package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.GetEventsAdminRequest;
import ru.practicum.explorewithme.service.dto.event.GetEventsUserRequest;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.ParticipationRequest;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.enums.EventState;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;
import ru.practicum.explorewithme.service.enums.UpdateEventAdminAction;
import ru.practicum.explorewithme.service.exception.ConflictException;
import ru.practicum.explorewithme.service.exception.ForbiddenException;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.EventMapper;
import ru.practicum.explorewithme.service.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.service.repository.CategoryRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.service.service.EventService;
import ru.practicum.explorewithme.service.specs.EventSpecifications;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEventService implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper requestMapper;

    @Override
    @Transactional
    public EventFullDto save(User user, NewEventDto request) {
        Category category = fetchCategory(request.getCategory());
        Event event = eventMapper.toNewEvent(user, category, request);
        event = eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public ParticipationRequestDto saveParticipationRequest(User user, Long eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(user.getId(), eventId)) {
            throw new ConflictException("Integrity constraint has been violated");
        }

        Event event = fetchEvent(eventId);
        checkEventParticipationPreconditions(user, event);

        ParticipationStatus participantStatus = event.getRequestModeration()
                ? ParticipationStatus.PENDING
                : ParticipationStatus.CONFIRMED;
        if (event.getParticipantLimit().equals(0)) {
            participantStatus = ParticipationStatus.CONFIRMED;
        }
        ParticipationRequest participationRequest = new ParticipationRequest(event, user, Instant.now(), participantStatus);

        requestRepository.save(participationRequest);

        return requestMapper.toDto(participationRequest);
    }

    private void checkEventParticipationPreconditions(User user, Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot request to not published event");
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Initiator cannot request to his own event");
        }
        if (isLimitReached(event)) {
            throw new ConflictException("Participant limit is reached");
        }
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateParticipationStatus(User user, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = fetchEvent(eventId);
        List<ParticipationRequest> participationRequests = requestRepository.findAllById(updateRequest.getRequestIds());

        checkParticipationLimit(event);

        int participantLimit = event.getParticipantLimit();
        int confirmedRequestsCount = event.getConfirmedRequests();

        ArrayList<ParticipationRequest> confirmedRequests = new ArrayList<>(updateRequest.getRequestIds().size());
        ArrayList<ParticipationRequest> rejectedRequests = new ArrayList<>(updateRequest.getRequestIds().size());

        for (ParticipationRequest request : participationRequests) {
            // if limit is reached
            if (confirmedRequestsCount > participantLimit) {
                rejectedRequests.add(request);
            } else {
                confirmedRequests.add(request);
                confirmedRequestsCount++;
            }
        }

        event.setConfirmedRequests(confirmedRequestsCount);

        requestRepository.saveAll(participationRequests);

        return new EventRequestStatusUpdateResult(
                confirmedRequests.stream().map(requestMapper::toDto).toList(),
                rejectedRequests.stream().map(requestMapper::toDto).toList()
        );
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(User user) {
        return requestRepository.findAllByRequesterId(user.getId()).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(User user, Long requestId) {
        ParticipationRequest participationRequest = fetchRequest(requestId);
        participationRequest.setStatus(ParticipationStatus.REJECTED);
        participationRequest = requestRepository.save(participationRequest);
        return requestMapper.toDto(participationRequest);
    }

    @Override
    // todo: save in stats service
    public List<EventShortDto> get(GetEventsUserRequest request) {
        return eventRepository.findAll(
                        Specification.allOf(getUserRequestSpecification(request)),
                        request.getPageable()
                ).stream().map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    // todo: save in stats service
    public EventFullDto get(Long id) {
        return eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .map(eventMapper::toFullDto)
                .orElseThrow(() -> new NotFoundException(Event.class, id));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        final Event event = eventRepository.safeFetch(eventId);

        checkStartAndPublishDateDuration(event);
        checkPublishingAction(updateRequest, event);
        checkRejectingAction(updateRequest, event);
        updateRequest.getStateAction().updateEventState(event);

        Optional.ofNullable(updateRequest.getCategory())
                .ifPresent(categoryId -> event.setCategory(fetchCategory(categoryId)));
        Optional.ofNullable(updateRequest.getLocation())
                .ifPresent(event::setLocation);

        eventMapper.updateFields(event, updateRequest);
        return eventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> get(GetEventsAdminRequest request) {
        return eventRepository.findAll(
                        Specification.allOf(getAdminRequestSpecification(request)),
                        request.getPageable()).stream()
                .map(eventMapper::toFullDto)
                .toList();
    }

    @Override
    public List<EventShortDto> getUsersEvents(User user, int from, int size) {
        return eventRepository.findAllByInitiatorId(user.getId(), PageRequest.of(from, size)).stream()
                .map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto getEvent(User user, Long eventId) {
        return eventRepository.findByInitiatorIdAndId(user.getId(), eventId)
                .map(eventMapper::toFullDto)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
    }

    @Override // todo
    public List<ParticipationRequestDto> getRequests(User user, Long eventId) {
        Event event = fetchEvent(eventId);
        return requestRepository.findAllByRequesterIdAndEventId(user.getId(), event.getId()).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto edit(User user, Long eventId, UpdateEventUserRequest updateRequest) {
        final Event event = fetchEvent(eventId);
        if (!(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    FORBIDDEN, "Only pending or canceled events can be changed");
        }

        if (updateRequest.getStateAction() != null) {
            updateRequest.getStateAction().updateEventState(event);
        }

        Optional.ofNullable(updateRequest.getCategory())
                .ifPresent(categoryId -> event.setCategory(fetchCategory(categoryId)));

        eventMapper.updateFields(event, updateRequest);
        return eventMapper.toFullDto(event);
    }

    private Event fetchEvent(Long eventId) {
        return eventRepository.safeFetch(eventId);
    }

    private Category fetchCategory(Long id) {
        return categoryRepository.safeFetch(id);
    }

    private ParticipationRequest fetchRequest(Long requestId) {
        return requestRepository.safeFetch(requestId);
    }

    private void checkParticipationLimit(Event event) {
        Integer eventRequestsCount = requestRepository.countAllByEventId(event.getId());
        if (eventRequestsCount.equals(event.getConfirmedRequests())) {
            throw new ConflictException("The participant limit has been reached");
        }
    }

    private boolean isLimitReached(Event event) {
        if (event.getParticipantLimit().equals(0)) {
            return false;
        }
        return !(event.getParticipantLimit() > event.getConfirmedRequests());
    }

    private List<Specification<Event>> getUserRequestSpecification(GetEventsUserRequest request) {
        ArrayList<Specification<Event>> specifications = new ArrayList<>();
        if (request.getText() != null) {
            Specification<Event> specification = EventSpecifications.text(request.getText());
            specifications.add(specification);
        }
        if (request.getPaid() != null) {
            Specification<Event> specification = EventSpecifications.paid(request.getPaid());
            specifications.add(specification);
        }
        if (request.getStart() != null && request.getEnd() != null) {
            Specification<Event> specification = EventSpecifications.startAndEnd(request.getStart(), request.getEnd());
            specifications.add(specification);
        } else {
            Specification<Event> specification = EventSpecifications.defaultStartAndEnd();
            specifications.add(specification);
        }
        if (request.getOnlyAvailable()) {
            Specification<Event> specification = EventSpecifications.onlyAvailable();
            specifications.add(specification);
        }
        if (!CollectionUtils.isEmpty(request.getCategories())) {
            Specification<Event> specification = EventSpecifications.categories(request.getCategories());
            specifications.add(specification);
        }
        return specifications;
    }

    private List<Specification<Event>> getAdminRequestSpecification(GetEventsAdminRequest request) {
        ArrayList<Specification<Event>> specifications = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getCategories())) {
            Specification<Event> specification = EventSpecifications.categories(request.getCategories());
            specifications.add(specification);
        }
        if (!CollectionUtils.isEmpty(request.getUsers())) {
            Specification<Event> specification = EventSpecifications.users(request.getUsers());
            specifications.add(specification);
        }
        if (request.getStart() != null && request.getEnd() != null) {
            Specification<Event> specification = EventSpecifications.startAndEnd(request.getStart(), request.getEnd());
            specifications.add(specification);
        } else {
            Specification<Event> specification = EventSpecifications.defaultStartAndEnd();
            specifications.add(specification);
        }
        return specifications;
    }

    private void checkRejectingAction(UpdateEventAdminRequest updateRequest, Event event) {
        if (event.getState().equals(EventState.PUBLISHED) &&
            updateRequest.getStateAction().equals(UpdateEventAdminAction.REJECT_EVENT)) {
            throw new ConflictException("Cannot reject the event because it's not in the right state: CANCELED");
        }
    }

    private void checkPublishingAction(UpdateEventAdminRequest updateRequest, Event event) {
        if (!event.getState().equals(EventState.PENDING) &&
            updateRequest.getStateAction().equals(UpdateEventAdminAction.PUBLISH_EVENT)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
    }

    private void checkStartAndPublishDateDuration(Event event) {
        // if it is first time of event publishing
        if (event.getPublishedOn() == null) {
            return;
        }
        Instant eventStart = event.getEventDate();
        Instant publishedOn = event.getPublishedOn();
        Duration duration = Duration.between(eventStart, publishedOn);
        if (duration.toMinutes() >= 60) {
            throw new ConflictException("Difference between publish date and event start date is less than an hour");
        }
    }
}

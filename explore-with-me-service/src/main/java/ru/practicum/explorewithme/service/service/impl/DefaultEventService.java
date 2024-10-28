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
import ru.practicum.explorewithme.service.enums.EventRequestStatus;
import ru.practicum.explorewithme.service.enums.EventState;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;
import ru.practicum.explorewithme.service.enums.UpdateEventAdminAction;
import ru.practicum.explorewithme.service.exception.BadRequestException;
import ru.practicum.explorewithme.service.exception.ConflictException;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.EventMapper;
import ru.practicum.explorewithme.service.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.service.repository.CategoryRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.service.service.EventService;
import ru.practicum.explorewithme.service.specs.EventSpecifications;
import ru.practicum.explorewithme.statistics.client.StatisticsClient;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEventService implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper requestMapper;
    private final StatisticsClient statisticsClient;

    @Override
    @Transactional
    public EventFullDto save(User user, NewEventDto request) {
        Category category = fetchCategory(request.getCategory());
        Event event = eventMapper.toNewEvent(user, category, request);
        validateEventDate(event.getEventDate());
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
        ParticipationRequest participationRequest = requestMapper.toNewRequest(event, user, participantStatus);

        requestRepository.save(participationRequest);

        return requestMapper.toDto(participationRequest);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateParticipationStatus(User user, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        // Время создания запроса на участие в событии после подтверждения
        // должно соответствовать времени создания запроса на участие в событии указанного пользователя до подтверждения
        if (updateRequest == null) {
            throw new ConflictException("No request body");
        }
        Event event = fetchEvent(eventId);
        List<ParticipationRequest> participationRequests = requestRepository.findAllById(updateRequest.getRequestIds());

        checkParticipationLimit(event);

        int participantLimit = event.getParticipantLimit();
        int confirmedRequestsCount = event.getConfirmedRequests();

        ArrayList<ParticipationRequest> confirmedRequests = new ArrayList<>(updateRequest.getRequestIds().size());
        ArrayList<ParticipationRequest> rejectedRequests = new ArrayList<>(updateRequest.getRequestIds().size());

        for (ParticipationRequest request : participationRequests) {
            // if limit is reached or update request for rejecting
            if (confirmedRequestsCount > participantLimit || updateRequest.getStatus().equals(EventRequestStatus.REJECTED)) {
                request.setStatus(ParticipationStatus.REJECTED);
                rejectedRequests.add(request);
            } else {
                request.setStatus(ParticipationStatus.CONFIRMED);
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
        ParticipationRequest participationRequest = requestRepository.findByIdAndRequesterId(requestId, user.getId())
                .orElseThrow(() -> new NotFoundException(ParticipationRequest.class, requestId));
        participationRequest.setStatus(ParticipationStatus.CANCELED);
        participationRequest = requestRepository.save(participationRequest);
        return requestMapper.toDto(participationRequest);
    }

    @Override
    public List<EventShortDto> get(GetEventsUserRequest request) {
        return eventRepository.findAll(
                        Specification.allOf(getUserRequestSpecification(request)),
                        request.getPageable()
                ).stream().map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto get(Long id) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(Event.class, id));
        Long views = getViews(
                Instant.now().minus(14, ChronoUnit.DAYS),
                Instant.now().plus(14, ChronoUnit.DAYS),
                "/events/" + id
        );
        event.setViews(views);
        eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        final Event event = eventRepository.safeFetch(eventId);

        Optional.ofNullable(updateRequest.getEventDate())
                .ifPresent(this::validateEventDate);

        checkStartAndPublishDateDuration(event);
        checkPublishingAction(updateRequest, event);
        checkRejectingAction(updateRequest, event);
        if (updateRequest.getStateAction() != null) {
            updateRequest.getStateAction().updateEventState(event);
        }

        Optional.ofNullable(updateRequest.getCategory())
                .ifPresent(categoryId -> event.setCategory(fetchCategory(categoryId)));

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
        Event event = eventRepository.findByInitiatorIdAndId(user.getId(), eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        return requestRepository.findAllByEventId(event.getId()).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto edit(User user, Long eventId, UpdateEventUserRequest updateRequest) {
        final Event event = fetchEvent(eventId);
        validateEventStateForUpdate(event);

        Optional.ofNullable(updateRequest.getEventDate())
                .ifPresent(this::validateEventDate);

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

    private Long getViews(Instant start, Instant end, String uri) {
        String[] uris = new String[]{uri};
        log.info("Getting view from statistics service for uri: {}", uri);
        List<ViewStats> stats = statisticsClient.getStats(
                toLocalDateTime(start), toLocalDateTime(end), uris, true
        );
        log.info("Response from statistics service {}", stats);
        if (stats != null && !stats.isEmpty()) {
            return stats.getFirst().getHits();
        } else {
            return 0L;
        }
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
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

    private void validateEventDate(Instant instant) {
        Duration twoHours = Duration.ofHours(2);
        if (!instant.isAfter(Instant.now().plus(twoHours))) {
            throw new BadRequestException("Invalid event date");
        }
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

    private void validateEventStateForUpdate(Event event) {
        if (!(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            throw new ConflictException(
                    "For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed");
        }
    }
}

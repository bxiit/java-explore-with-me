package ru.practicum.explorewithme.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.GetEventsRequest;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.ParticipationRequest;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.enums.EventState;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;
import ru.practicum.explorewithme.service.exception.ConflictException;
import ru.practicum.explorewithme.service.exception.ForbiddenException;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.EventMapper;
import ru.practicum.explorewithme.service.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.service.repository.CategoryRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.service.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEventService implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper requestMapper;

    @Override
    @Transactional
    public EventFullDto save(User user, NewEventDto request) {
        Category category = fetchCategory(request.getCategory());
        Event event = eventMapper.toEntity(user, category, request);
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
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot request to not published event");
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Initiator cannot request to his own event");
        }
        if (isLimitReached(event)) {
            throw new ConflictException("Participant limit is reached");
        }

        ParticipationRequest participationRequest;
        if (!event.getRequestModeration()) {
            participationRequest = new ParticipationRequest(event, user, Instant.now(), ParticipationStatus.APPROVED);
        } else {
            participationRequest = new ParticipationRequest(event, user, Instant.now(), ParticipationStatus.PENDING);
        }

        participationRequest = requestRepository.save(participationRequest);

        return ParticipationRequestDto.builder()
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .created(participationRequest.getCreated())
                .status(participationRequest.getStatus())
                .build();
    }

    private boolean isLimitReached(Event event) {
        return !(event.getParticipantLimit() > event.getConfirmedRequests());
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
    public List<EventShortDto> get(GetEventsRequest request) {
        List<Specification<Event>> specifications = getSpecification(request);
        return eventRepository.findAll(
                        Specification.allOf(specifications),
                        PageRequest.of(request.getFrom(), request.getSize(), request.getSort().getSort())
                ).stream().map(eventMapper::toShortDto)
                .toList();
    }

    private List<Specification<Event>> getSpecification(GetEventsRequest request) {
        ArrayList<Specification<Event>> specifications = new ArrayList<>();
        if (request.getText() != null) {
            Specification<Event> specification = EventRepository.text(request.getText());
            specifications.add(specification);
        }
        if (request.getPaid() != null) {
            Specification<Event> specification = EventRepository.paid(request.getPaid());
            specifications.add(specification);
        }
        if (request.getStart() != null && request.getEnd() != null) {
            Specification<Event> specification = EventRepository.startAndEnd(request.getStart(), request.getEnd());
            specifications.add(specification);
        } else {
            Specification<Event> specification = EventRepository.defaultStartAndEnd();
            specifications.add(specification);
        }
        if (request.isOnlyAvailable()) {
            Specification<Event> specification = EventRepository.onlyAvailable();
            specifications.add(specification);
        }
        if (request.getCategories() != null && !request.getCategories().isEmpty()) {
            Specification<Event> specification = EventRepository.categories(request.getCategories());
            specifications.add(specification);
        }
        return specifications;
    }

    @Override
    public List<EventShortDto> getUsersEvents(User user, int from, int size) {
        return eventRepository.findAllByInitiatorId(user.getId(), PageRequest.of(from, size)).stream()
                .map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto getEvent(User user, Long eventId) {
        Event event = fetchEvent(eventId);
        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new NotFoundException(Event.class, eventId);
        }
        return eventMapper.toFullDto(event);
    }

    @Override // todo
    public ParticipationRequestDto getRequests(User user, Long eventId) {
        Event event = fetchEvent(eventId);
        return null;
    }

    @Override
    @Transactional
    public EventFullDto edit(User user, Long eventId, UpdateEventUserRequest request) {
        Event event = fetchEvent(eventId);
        if (!(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    FORBIDDEN, "Only pending or canceled events can be changed");
        }
        Category category = fetchCategory(request.getCategory());
        event = eventMapper.updateFields(event, request, category);
        return eventMapper.toFullDto(event);
    }

    private Event fetchEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
    }

    private Category fetchCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Category.class, id));
    }

    private ParticipationRequest fetchRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ParticipationRequest.class, requestId));
    }

    private void checkParticipationLimit(Event event) {
        Integer eventRequestsCount = requestRepository.countAllByEventId(event.getId());
        if (eventRequestsCount.equals(event.getConfirmedRequests())) {
            throw new ConflictException("The participant limit has been reached");
        }
    }
}

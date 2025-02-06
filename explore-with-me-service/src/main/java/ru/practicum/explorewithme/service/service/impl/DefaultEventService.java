package ru.practicum.explorewithme.service.service.impl;

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
import ru.practicum.explorewithme.service.dto.event.GetEventsAdminRequest;
import ru.practicum.explorewithme.service.dto.event.GetEventsUserRequest;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dto.rate.EventRate;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.ParticipationRequest;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.enums.EventState;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;
import ru.practicum.explorewithme.service.enums.UpdateEventAdminAction;
import ru.practicum.explorewithme.service.exception.ConflictException;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.EventMapper;
import ru.practicum.explorewithme.service.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.service.repository.CategoryRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.service.repository.UserRepository;
import ru.practicum.explorewithme.service.service.EventService;
import ru.practicum.explorewithme.service.service.RateService;
import ru.practicum.explorewithme.statistics.client.StatisticsClient;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static ru.practicum.explorewithme.service.specs.EventSpecifications.getAdminRequestSpecification;
import static ru.practicum.explorewithme.service.specs.EventSpecifications.getUserRequestSpecification;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultEventService implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper requestMapper;
    private final StatisticsClient statisticsClient;
    private final UserRepository userRepository;
    private final RateService rateService;

    @Override
    @Transactional
    public EventFullDto save(Long userId, NewEventDto request) {
        Category category = fetchCategory(request.getCategory());
        User user = fetchUser(userId);
        Event event = eventMapper.toNewEvent(user, category, request);
        event = eventRepository.save(event);
        return eventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        User user = fetchUser(userId);
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Integrity constraint has been violated");
        }

        Event event = fetchEvent(eventId);
        validateEventParticipationPreconditions(userId, event);

        ParticipationStatus participantStatus = determineParticipationStatus(event);
        ParticipationRequest participationRequest = requestMapper.toNewRequest(event, user, participantStatus);

        requestRepository.save(participationRequest);

        return requestMapper.toDto(participationRequest);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateParticipationStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest
    ) {
        // Время создания запроса на участие в событии после подтверждения
        // должно соответствовать времени создания запроса на участие в событии указанного пользователя до подтверждения
        if (updateRequest == null) {
            throw new ConflictException("No request body");
        }
        Event event = fetchEvent(eventId);
        List<ParticipationRequest> participationRequests = requestRepository.findAllById(updateRequest.getRequestIds());

        validateParticipationLimit(event);

        Map<ParticipationStatus, List<ParticipationRequest>> results =
                processEventParticipationRequests(updateRequest, participationRequests, event);

        requestRepository.saveAll(participationRequests);

        return new EventRequestStatusUpdateResult(
                results.getOrDefault(ParticipationStatus.CONFIRMED, emptyList()).stream().map(requestMapper::toDto).toList(),
                results.getOrDefault(ParticipationStatus.REJECTED, emptyList()).stream().map(requestMapper::toDto).toList()
        );
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        checkUserExistence(userId);
        return requestRepository.findAllByRequesterId(userId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        boolean exists = eventRepository.existsByInitiatorIdAndId(userId, eventId);
        if (!exists) {
            throw new NotFoundException(Event.class, eventId);
        }
        return requestRepository.findAllByEventId(eventId).stream()
                .map(requestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        checkUserExistence(userId);
        ParticipationRequest participationRequest = requestRepository.findByIdAndRequesterId(requestId, userId)
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
                LocalDateTime.now().minusDays(14),
                LocalDateTime.now().plusDays(14),
                "/events/" + id
        );
        event.setViews(views);
        eventRepository.save(event);
        EventFullDto eventFullDto = eventMapper.toFullDto(event);
        eventFullDto.setRate(rateService.getEventRating(id));
        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        final Event event = fetchEvent(eventId);

        validateStartAndPublishDateDuration(event);
        validatePublishingAction(updateRequest, event);
        validateRejectingAction(updateRequest, event);
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
        List<EventFullDto> events = eventRepository.findAll(
                        Specification.allOf(getAdminRequestSpecification(request)),
                        request.getPageable()).stream()
                .map(eventMapper::toFullDto)
                .toList();

        List<Long> eventsIds = events.stream().map(EventFullDto::getId).toList();
        Map<Long, EventRate> eventRateMap = rateService.getEventsRatings(eventsIds).stream()
                .collect(Collectors.toMap(EventRate::getEventId, Function.identity()));

        events.forEach(event -> event.setRate(eventRateMap.get(event.getId())));

        return events;
    }

    @Override
    public List<EventShortDto> getUsersEvents(Long userId, int from, int size) {
        checkUserExistence(userId);
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size)).stream()
                .map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        checkUserExistence(userId);
        return eventRepository.findByInitiatorIdAndId(userId, eventId)
                .map(eventMapper::toFullDto)
                .map(event -> {
                    event.setRate(rateService.getEventRating(eventId));
                    return event;
                })
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
    }

    @Override
    @Transactional
    public EventFullDto edit(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        final Event event = fetchEvent(eventId);
        validateEventStateForUpdate(event);

        Optional.ofNullable(updateRequest.getStateAction())
                .ifPresent(action -> action.updateEventState(event));

        Optional.ofNullable(updateRequest.getCategory())
                .ifPresent(categoryId -> event.setCategory(fetchCategory(categoryId)));

        eventMapper.updateFields(event, updateRequest);
        return eventMapper.toFullDto(event);
    }

    private ParticipationStatus determineParticipationStatus(Event event) {
        boolean confirm = event.isLimitless() || !event.getRequestModeration();
        if (confirm) {
            return ParticipationStatus.CONFIRMED;
        } else {
            return ParticipationStatus.PENDING;
        }
    }

    private void checkUserExistence(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(User.class, userId);
        }
    }

    private Map<ParticipationStatus, List<ParticipationRequest>> processEventParticipationRequests(
            EventRequestStatusUpdateRequest updateRequest,
            List<ParticipationRequest> participationRequests,
            Event event
    ) {
        return participationRequests.stream()
                .collect(Collectors.groupingBy(request -> {
                    if (updateRequest.isReject()) {
                        return request.reject();
                    } else {
                        event.confirmRequest();
                        return request.confirm();
                    }
                }));
    }

    private User fetchUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }

    private Event fetchEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Event.class, id));
    }

    private Category fetchCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Category.class, id));
    }

    private Long getViews(LocalDateTime start, LocalDateTime end, String uri) {
        String[] uris = new String[]{uri};
        log.info("Getting view from statistics service for uri: {}", uri);
        List<ViewStats> stats = statisticsClient.getStats(
                start, end, uris, true
        );
        log.info("Response from statistics service {}", stats);
        if (stats != null && !stats.isEmpty()) {
            return stats.getFirst().getHits();
        } else {
            return 0L;
        }
    }

    private void validateEventParticipationPreconditions(Long userId, Event event) {
        if (!event.isPublished()) {
            throw new ConflictException("Cannot request to not published event");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator cannot request to his own event");
        }
        if (isLimitReached(event)) {
            throw new ConflictException("Participant limit is reached");
        }
    }

    private void validateParticipationLimit(Event event) {
        Integer eventRequestsCount = requestRepository.countAllByEventIdAndStatusIn(
                event.getId(), List.of(ParticipationStatus.CONFIRMED)
        );

        if (eventRequestsCount.equals(event.getParticipantLimit())) {
            throw new ConflictException("The participant limit has been reached");
        }
    }

    private boolean isLimitReached(Event event) {
        if (event.isLimitless()) {
            return false;
        }

        Integer count = requestRepository.countAllByEventIdAndStatusIn(event.getId(),
                List.of(
                        ParticipationStatus.CONFIRMED
                ));

        return count.equals(event.getParticipantLimit());
    }

    private void validateRejectingAction(UpdateEventAdminRequest updateRequest, Event event) {
        if (event.getState().equals(EventState.PUBLISHED) &&
            updateRequest.getStateAction().equals(UpdateEventAdminAction.REJECT_EVENT)) {
            throw new ConflictException("Cannot reject the event because it's not in the right state: CANCELED");
        }
    }

    private void validatePublishingAction(UpdateEventAdminRequest updateRequest, Event event) {
        if (!event.getState().equals(EventState.PENDING) &&
            updateRequest.getStateAction().equals(UpdateEventAdminAction.PUBLISH_EVENT)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
    }

    private void validateStartAndPublishDateDuration(Event event) {
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

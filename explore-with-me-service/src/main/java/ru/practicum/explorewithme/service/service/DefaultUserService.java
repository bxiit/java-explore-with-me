package ru.practicum.explorewithme.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.explorewithme.service.controller.UserService;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final EventService eventService;
    private final UserRepository userRepository;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(Long userId, NewEventDto request) {
        User user = fetchUser(userId);
        return eventService.save(user, request);
    }

    @Override
    public List<EventShortDto> get(Long userId, int from, int size) {
        User user = fetchUser(userId);
        return eventService.getUsersEvents(user, from, size);
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        User user = fetchUser(userId);
        return eventService.getEvent(user, eventId);
    }

    @Override
    public EventFullDto edit(Long userId, Long eventId, UpdateEventUserRequest request) {
        User user = fetchUser(userId);
        return eventService.edit(user, eventId, request);
    }

    @Override
    public ParticipationRequestDto getRequests(Long userId, Long eventId) {
        User user = fetchUser(userId);
        return eventService.getRequests(user, eventId);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        User user = fetchUser(userId);
        return eventService.saveParticipationRequest(user, eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateEventParticipationStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        User user = fetchUser(userId);
        return eventService.updateParticipationStatus(user, eventId, request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        User user = fetchUser(userId);
        return eventService.getUserRequests(user);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = fetchUser(userId);
        return eventService.cancelRequest(user, requestId);
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, userId));
    }
}

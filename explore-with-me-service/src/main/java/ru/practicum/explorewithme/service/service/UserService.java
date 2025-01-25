package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dto.user.GetUsersAdminRequest;
import ru.practicum.explorewithme.service.dto.user.NewUserRequest;
import ru.practicum.explorewithme.service.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto saveNewEvent(Long userId, NewEventDto request);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto editEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventParticipationStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<UserDto> get(GetUsersAdminRequest request);

    UserDto save(NewUserRequest request);

    void deleteUser(Long userId);
}

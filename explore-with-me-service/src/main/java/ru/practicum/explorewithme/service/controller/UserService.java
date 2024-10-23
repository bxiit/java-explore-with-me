package ru.practicum.explorewithme.service.controller;

import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;

import java.util.List;

public interface UserService {
    List<EventShortDto> get(Long userId, int from, int size);

    EventFullDto save(Long userId, NewEventDto request);

    EventFullDto get(Long userId, Long eventId);

    EventFullDto edit(Long userId, Long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventParticipationStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}

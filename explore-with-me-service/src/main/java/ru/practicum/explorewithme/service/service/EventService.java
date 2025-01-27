package ru.practicum.explorewithme.service.service;

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

import java.util.List;

public interface EventService {
    List<EventShortDto> getUsersEvents(Long user, int from, int size);

    EventFullDto save(Long user, NewEventDto request);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto edit(Long userId, Long eventId, UpdateEventUserRequest request);

    ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipationStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<EventShortDto> get(GetEventsUserRequest request);

    EventFullDto get(Long id);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest);

    List<EventFullDto> get(GetEventsAdminRequest request);
}

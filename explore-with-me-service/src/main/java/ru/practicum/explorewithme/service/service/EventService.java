package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.event.GetEventsRequest;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.entity.User;

import java.util.List;

public interface EventService {
    List<EventShortDto> getUsersEvents(User user, int from, int size);

    EventFullDto save(User user, NewEventDto request);

    EventFullDto getEvent(User user, Long eventId);

    EventFullDto edit(User user, Long eventId, UpdateEventUserRequest request);

    ParticipationRequestDto getRequests(User user, Long eventId);

    ParticipationRequestDto saveParticipationRequest(User user, Long eventId);

    EventRequestStatusUpdateResult updateParticipationStatus(User user, Long eventId, EventRequestStatusUpdateRequest request);

    List<ParticipationRequestDto> getUserRequests(User user);

    ParticipationRequestDto cancelRequest(User user, Long requestId);

    List<EventShortDto> get(GetEventsRequest request);
}

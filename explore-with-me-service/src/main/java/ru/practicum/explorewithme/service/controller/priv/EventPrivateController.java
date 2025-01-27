package ru.practicum.explorewithme.service.controller.priv;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> get(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return eventService.getUsersEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable("userId") Long userId, @RequestBody @Valid NewEventDto request) {
        return eventService.save(userId, request);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto get(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto edit(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @RequestBody @Valid UpdateEventUserRequest request) {
        return eventService.edit(userId, eventId, request);
    }

    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEvent(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId,
            @RequestBody(required = false) @Valid EventRequestStatusUpdateRequest request
    ) {
        return eventService.updateParticipationStatus(userId, eventId, request);
    }
}

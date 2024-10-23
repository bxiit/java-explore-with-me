package ru.practicum.explorewithme.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> get(
            @PathVariable("userId") Long userId,
            @RequestParam int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.get(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto save(@PathVariable("userId") Long userId, @RequestBody @Valid NewEventDto request) {
        return userService.save(userId, request);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto get(
            @PathVariable Long eventId,
            @PathVariable Long userId
    ) {
        return userService.get(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto edit(
            @PathVariable("userId") Long userId,
            @PathVariable("eventId") Long eventId,
            @RequestBody @Valid UpdateEventUserRequest request) {
        return userService.edit(userId, eventId, request);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId
    ) {
        return userService.getRequests(userId, eventId);
    }

    //Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEvent(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId,
            @RequestBody @Valid EventRequestStatusUpdateRequest request
    ) {
        return userService.updateEventParticipationStatus(userId, eventId, request);
    }

    // Private: Запросы на участие
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(
            @PathVariable("userId") Long userId
    ) {
        return userService.getUserRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto saveParticipationRequest(
            @PathVariable("userId") Long userId,
            @RequestParam("eventId") Long eventId
    ) {
        return userService.saveParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable("requestId") Long requestId, @PathVariable("userId") Long userId
    ) {
        return userService.cancelRequest(userId, requestId);
    }
}

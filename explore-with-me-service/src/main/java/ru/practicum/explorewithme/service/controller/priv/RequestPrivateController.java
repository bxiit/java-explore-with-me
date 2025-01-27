package ru.practicum.explorewithme.service.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestPrivateController {

    private final EventService eventService;

    // Private: Запросы на участие
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable("userId") Long userId
    ) {
        return eventService.getUserRequests(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto saveParticipationRequest(
            @PathVariable("userId") Long userId,
            @RequestParam("eventId") Long eventId
    ) {
        return eventService.saveParticipationRequest(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId
    ) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable("requestId") Long requestId, @PathVariable("userId") Long userId
    ) {
        return eventService.cancelRequest(userId, requestId);
    }
}

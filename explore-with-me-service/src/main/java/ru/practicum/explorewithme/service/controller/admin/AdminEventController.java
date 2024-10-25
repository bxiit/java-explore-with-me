package ru.practicum.explorewithme.service.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.GetEventsAdminRequest;
import ru.practicum.explorewithme.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.service.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> get(
            @Valid GetEventsAdminRequest request
    ) {
        return eventService.get(request);
    }

    // accept or decline event
    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("eventId") Long eventId,
                               @RequestBody @Valid UpdateEventAdminRequest updateRequest) {
        return eventService.updateEvent(eventId, updateRequest);
    }
}

package ru.practicum.explorewithme.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.GetEventsRequest;
import ru.practicum.explorewithme.service.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> get(
            GetEventsRequest request
    ) {
        return eventService.get(request);
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable("id") Long id) {
        return eventService.get(id);
    }
}

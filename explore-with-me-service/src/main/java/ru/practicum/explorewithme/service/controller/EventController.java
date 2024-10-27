package ru.practicum.explorewithme.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.GetEventsUserRequest;
import ru.practicum.explorewithme.service.service.EventService;
import ru.practicum.explorewithme.service.util.AppConstants;
import ru.practicum.explorewithme.statistics.client.StatisticsClient;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final StatisticsClient statisticsClient;

    @GetMapping
    public List<EventShortDto> get(@Valid GetEventsUserRequest request, HttpServletRequest requestInfo) {
        saveEndpointHit(requestInfo);
        return eventService.get(request);
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable("id") Long id, HttpServletRequest requestInfo) {
        saveEndpointHit(requestInfo);
        return eventService.get(id);
    }

    private void saveEndpointHit(HttpServletRequest requestInfo) {
        log.info("Save to statistics service");
        ResponseEntity<Object> response = statisticsClient.saveHit(
                new EndpointHit(
                        "ewm-main-service", requestInfo.getRequestURI(), requestInfo.getRemoteAddr(),
                        formatNow()
                )
        );
        log.info("Response from statistics service {}", response.toString());
    }

    private String formatNow() {
        return AppConstants.DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }
}

package ru.practicum.explorewithme.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.GetEventsUserRequest;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackFullDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackShortDto;
import ru.practicum.explorewithme.service.dto.rate.EventRate;
import ru.practicum.explorewithme.service.service.EventService;
import ru.practicum.explorewithme.service.service.RateService;
import ru.practicum.explorewithme.statistics.client.StatisticsClient;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final StatisticsClient statisticsClient;
    private final RateService rateService;

    @GetMapping
    public List<EventShortDto> get(@Valid GetEventsUserRequest request, HttpServletRequest requestInfo) {
        statisticsClient.hit(requestInfo);
        return eventService.get(request);
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable("id") Long id, HttpServletRequest requestInfo) {
        statisticsClient.hit(requestInfo);
        return eventService.get(id);
    }

    @PostMapping("/feedbacks")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFeedbackFullDto saveEventFeedback(@RequestBody EventFeedbackDto eventFeedbackDto) {
        return rateService.saveEventFeedback(eventFeedbackDto);
    }

    @GetMapping("/{eventId}/feedbacks")
    public List<EventFeedbackShortDto> getEventFeedbacks(@PathVariable Long eventId, Pageable pageable) {
        return rateService.getEventFeedbacks(eventId, pageable);
    }

    @GetMapping("/{eventId}/feedbacks/ratings")
    public EventRate getEventRating(@PathVariable Long eventId) {
        return rateService.getEventRating(eventId);
    }
}

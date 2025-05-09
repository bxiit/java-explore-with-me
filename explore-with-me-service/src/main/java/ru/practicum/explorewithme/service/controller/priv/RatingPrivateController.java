package ru.practicum.explorewithme.service.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackFullDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackShortDto;
import ru.practicum.explorewithme.service.service.RateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/ratings")
public class RatingPrivateController {

    private final RateService rateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFeedbackFullDto saveEventFeedback(@RequestBody EventFeedbackDto eventFeedbackDto) {
        return rateService.saveEventFeedback(eventFeedbackDto);
    }

    @GetMapping("/{eventId}/feedbacks")
    public List<EventFeedbackShortDto> getEventFeedbacks(@PathVariable Long eventId, Pageable pageable) {
        return rateService.getEventFeedbacks(eventId, pageable);
    }
}

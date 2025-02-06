package ru.practicum.explorewithme.service.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackFullDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackShortDto;
import ru.practicum.explorewithme.service.dto.rate.EventRate;

import java.util.List;

public interface RateService {
    EventFeedbackFullDto saveEventFeedback(EventFeedbackDto eventFeedbackDto);

    List<EventFeedbackShortDto> getEventFeedbacks(Long eventId, Pageable pageable);

    EventRate getEventRating(Long eventId);

    List<EventRate> getEventsRatings(List<Long> eventIds);
}
package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackFullDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackShortDto;
import ru.practicum.explorewithme.service.dto.rate.EventRate;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.EventFeedback;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.enums.Feedback;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.RateMapper;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.repository.RateRepository;
import ru.practicum.explorewithme.service.repository.UserRepository;
import ru.practicum.explorewithme.service.service.RateService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static ru.practicum.explorewithme.service.enums.Feedback.DISLIKE;
import static ru.practicum.explorewithme.service.enums.Feedback.LIKE;

@Service
@RequiredArgsConstructor
public class DefaultRateService implements RateService {

    private final RateRepository rateRepository;
    private final RateMapper rateMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public EventFeedbackFullDto saveEventFeedback(EventFeedbackDto eventFeedbackDto) {
        Optional<EventFeedback> maybeEventRate = rateRepository.findByEventIdAndUserId(eventFeedbackDto.getEventId(), eventFeedbackDto.getUserId());
        if (maybeEventRate.isPresent()) {
            EventFeedback existingEventFeedback = maybeEventRate.get();
            existingEventFeedback.setFeedback(eventFeedbackDto.getFeedback());
            existingEventFeedback.modify();
            rateRepository.save(existingEventFeedback);
            return rateMapper.toRateFullDto(existingEventFeedback);
        }
        Event event = fetchEvent(eventFeedbackDto.getEventId());
        User user = fetchUser(eventFeedbackDto.getUserId());

        EventFeedback eventFeedback = rateMapper.toEntity(eventFeedbackDto, event, user);

        rateRepository.save(eventFeedback);
        return rateMapper.toRateFullDto(eventFeedback);
    }

    @Override
    public List<EventFeedbackShortDto> getEventFeedbacks(Long eventId, Pageable pageable) {
        return rateRepository.findAllByEventId(eventId, pageable).stream()
                .map(rateMapper::toRateShortDto)
                .toList();
    }

    @Override
    public EventRate getEventRating(Long eventId) {
        Map<Feedback, List<EventFeedback>> feedbacksMap = rateRepository.findAllByEventId(eventId).stream()
                .collect(Collectors.groupingBy(EventFeedback::getFeedback));

        List<EventFeedbackShortDto> likes = feedbacksMap.getOrDefault(LIKE, emptyList()).stream()
                .map(rateMapper::toRateShortDto)
                .collect(Collectors.toList());

        List<EventFeedbackShortDto> dislikes = feedbacksMap.getOrDefault(DISLIKE, emptyList()).stream()
                .map(rateMapper::toRateShortDto)
                .collect(Collectors.toList());

        int likesCount = likes.size();
        int dislikesCount = dislikes.size();

        int ratingsCount = likesCount + dislikesCount;
        // 1000 f  // 200
        // 100 %   // X %
        double rating = ((double) (likesCount * 100) / ratingsCount);
        return new EventRate(likes, dislikes, ratingsCount, rating);
    }

    private Event fetchEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
    }

    private User fetchUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Event.class, userId));
    }
}

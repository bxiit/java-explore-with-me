package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackFullDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackShortDto;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.EventFeedback;
import ru.practicum.explorewithme.service.entity.User;

@Mapper(uses = {UserMapper.class, EventMapper.class})
public interface RateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    EventFeedback toEntity(EventFeedbackDto eventFeedbackDto, Event event, User user);

    EventFeedbackFullDto toRateFullDto(EventFeedback eventFeedback);

    EventFeedbackShortDto toRateShortDto(EventFeedback eventFeedback);
}

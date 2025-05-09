package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackFullDto;
import ru.practicum.explorewithme.service.dto.rate.EventFeedbackShortDto;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.Rate;
import ru.practicum.explorewithme.service.entity.User;

@Mapper(uses = {UserMapper.class, EventMapper.class})
public interface RateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Rate toEntity(EventFeedbackDto eventFeedbackDto, Event event, User user);

    EventFeedbackFullDto toRateFullDto(Rate rate);

    EventFeedbackShortDto toRateShortDto(Rate rate);
}

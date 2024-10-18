package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.entity.Event;

@Mapper
public interface EventMapper {
    EventShortDto toShortDto(Event event);
}

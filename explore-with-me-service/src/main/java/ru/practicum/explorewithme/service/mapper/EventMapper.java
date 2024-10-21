package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.User;

@Mapper(uses = {UserMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EventMapper {
    EventShortDto toShortDto(Event event);

    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "category", source = "category")
    Event toEntity(User user, Category category, NewEventDto request);

    @Mapping(target = "createdOn", source = "created")
    EventFullDto toFullDto(Event event);

    @Mapping(target = "category", source = "category")
    Event updateFields(@MappingTarget Event event, UpdateEventUserRequest request, Category category);
}

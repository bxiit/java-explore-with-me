package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventAdminRequest;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "compilations", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "confirmedRequests", expression = "java(0)")
    @Mapping(target = "state", expression = "java(ru.practicum.explorewithme.service.enums.EventState.PENDING)")
    Event toNewEvent(User user, Category category, NewEventDto request);

    @Mapping(target = "createdOn", source = "created")
    EventFullDto toFullDto(Event event);

    @Mapping(target = "category", ignore = true)
    void updateFields(@MappingTarget Event event, UpdateEventUserRequest request);

    @Mapping(target = "category", ignore = true)
    void updateFields(@MappingTarget Event event, UpdateEventAdminRequest updateRequest);
}

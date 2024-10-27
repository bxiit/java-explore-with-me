package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.entity.ParticipationRequest;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;

@Mapper
public interface ParticipationRequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toDto(ParticipationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requester", source = "user")
    @Mapping(target = "status", source = "participantStatus")
    ParticipationRequest toNewRequest(Event event, User user, ParticipationStatus participantStatus);
}

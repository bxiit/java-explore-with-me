package ru.practicum.explorewithme.service.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.enums.EventState;
import ru.practicum.explorewithme.service.util.InstantStringSerializer;

import java.time.Instant;

@Data
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonSerialize(using = InstantStringSerializer.class)
    private Instant createdOn;
    private String description;
    @JsonSerialize(using = InstantStringSerializer.class)
    private Instant eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit = 0; // Значение 0 - означает отсутствие ограничения
    @JsonSerialize(using = InstantStringSerializer.class)
    private Instant publishedOn;
    private Boolean requestModeration = true; // Нужна ли пре-модерация заявок на участие
    private EventState state;
    private String title;
    private Long views;
}

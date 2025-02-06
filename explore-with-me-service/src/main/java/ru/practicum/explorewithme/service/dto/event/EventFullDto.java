package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.rate.EventRate;
import ru.practicum.explorewithme.service.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.entity.Location;
import ru.practicum.explorewithme.service.enums.EventState;

import java.time.Instant;

@Data
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private Instant createdOn;
    private String description;
    private Instant eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit = 0; // Значение 0 - означает отсутствие ограничения
    private Instant publishedOn;
    private Boolean requestModeration = true; // Нужна ли пре-модерация заявок на участие
    private EventState state;
    private String title;
    private Long views;
    private EventRate rate;
}

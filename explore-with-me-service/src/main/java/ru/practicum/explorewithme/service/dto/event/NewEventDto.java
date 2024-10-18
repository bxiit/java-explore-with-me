package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;

import java.time.Instant;

@Data
public class NewEventDto {
    private String annotation;
    private Long category;
    private String description;
    private Instant eventDate;
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    private String title;
}

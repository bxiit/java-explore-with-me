package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;

import java.time.Instant;

@Data
public class UpdateEventUserRequest {
    private String annotation;
    private Long category;
    private String description;
    private Instant eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}

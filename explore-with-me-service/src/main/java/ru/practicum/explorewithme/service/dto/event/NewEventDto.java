package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;

import java.time.Instant;

@Data
public class NewEventDto {
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    private Instant eventDate;

    private Location location;

    private Boolean paid = false;

    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @Size(min = 3, max = 120)
    private String title;
}

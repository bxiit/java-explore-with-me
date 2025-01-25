package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.entity.Location;
import ru.practicum.explorewithme.service.enums.UpdateEventUserAction;
import ru.practicum.explorewithme.service.validation.MinDuration;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;

import java.time.Instant;

@Data
public class UpdateEventUserRequest {

    @NotOnlySpace
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @NotOnlySpace
    @Size(min = 20, max = 7000)
    private String description;

    @MinDuration(durationFromNow = "PT2H")
    private Instant eventDate;

    private Location location;

    private Boolean paid;

    @Positive
    private Integer participantLimit;

    private Boolean requestModeration;

    private UpdateEventUserAction stateAction;

    @NotOnlySpace
    @Size(min = 3, max = 120)
    private String title;
}

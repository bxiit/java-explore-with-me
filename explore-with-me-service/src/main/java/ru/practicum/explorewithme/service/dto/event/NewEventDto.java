package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.entity.Location;
import ru.practicum.explorewithme.service.validation.MinDuration;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;

import java.time.Instant;

@Data
public class NewEventDto {
    @NotNull
    @NotOnlySpace
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @NotOnlySpace
    @Size(min = 20, max = 7000)
    private String description;

    @MinDuration(durationFromNow = "PT2H")
    private Instant eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    @PositiveOrZero
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotEmpty
    @NotOnlySpace
    @Size(min = 3, max = 120)
    private String title;
}

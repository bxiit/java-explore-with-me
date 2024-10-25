package ru.practicum.explorewithme.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    @Positive
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotEmpty
    @Size(min = 3, max = 120)
    private String title;
}

package ru.practicum.explorewithme.service.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;
import ru.practicum.explorewithme.service.enums.UpdateEventAdminAction;
import ru.practicum.explorewithme.service.util.InstantStringDeserializer;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;

import java.time.Instant;

@Data
public class UpdateEventAdminRequest {

    @NotOnlySpace
    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @NotOnlySpace
    @Size(min = 20, max = 7000)
    private String description;

    @JsonDeserialize(using = InstantStringDeserializer.class)
    private Instant eventDate;

    private Location location;

    private Boolean paid;

    @Positive
    private Integer participantLimit;

    private Boolean requestModeration;

    private UpdateEventAdminAction stateAction;

    @NotOnlySpace
    @Size(max = 120, min = 3)
    private String title;
}

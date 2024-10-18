package ru.practicum.explorewithme.service.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;
import ru.practicum.explorewithme.service.dto.enums.StateAction;

import java.time.Instant;

@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private Long category;
    private String description;

    @JsonSerialize(using = InstantSerializer.class)
    private Instant eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;

    @Size(max = 120, min = 3)
    private String title;//maxLength: 120 minLength: 3
}

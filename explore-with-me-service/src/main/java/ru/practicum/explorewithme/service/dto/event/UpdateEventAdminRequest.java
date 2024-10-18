package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.dto.Location;
import ru.practicum.explorewithme.service.dto.enums.StateActionAdmin;

import java.time.Instant;

@Data
public class UpdateEventAdminRequest {
    private String annotation;
    private Long category;
    private String description;
    private Instant eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionAdmin stateAction;

    @Size(max = 120, min = 3)
    private String title;
}

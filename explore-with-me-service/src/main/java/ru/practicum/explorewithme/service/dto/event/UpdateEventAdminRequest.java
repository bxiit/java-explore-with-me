package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.entity.Location;
import ru.practicum.explorewithme.service.enums.UpdateEventAdminAction;
import ru.practicum.explorewithme.service.validation.MinDuration;

import java.time.Instant;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @MinDuration(durationFromNow = "PT2H")
    private Instant eventDate;

    private Location location;

    private Boolean paid;

    @Positive
    private Integer participantLimit;

    private Boolean requestModeration;

    private UpdateEventAdminAction stateAction;

    @Size(max = 120, min = 3)
    private String title;

    @AssertTrue(message = "invalid request")
    private boolean isValidRequest() {
        boolean isValidAnnotation = this.annotation == null || !annotation.isBlank();
        boolean isValidTitle = this.title == null || !title.isBlank();
        boolean isValidDescription = this.description == null || !description.isBlank();
        return isValidAnnotation &&
               isValidTitle &&
               isValidDescription;
    }
}

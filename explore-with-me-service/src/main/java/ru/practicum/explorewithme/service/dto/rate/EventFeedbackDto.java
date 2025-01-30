package ru.practicum.explorewithme.service.dto.rate;

import lombok.Data;
import ru.practicum.explorewithme.service.enums.Feedback;

@Data
public class EventFeedbackDto {
    private Long eventId;
    private Feedback feedback;
    private Long userId;
}

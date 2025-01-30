package ru.practicum.explorewithme.service.dto.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRate {
    private List<EventFeedbackShortDto> likes;
    private List<EventFeedbackShortDto> dislikes;
    private Integer ratingsCount;
    private Double rating;
}

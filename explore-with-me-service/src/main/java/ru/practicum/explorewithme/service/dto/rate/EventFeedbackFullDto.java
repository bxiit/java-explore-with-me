package ru.practicum.explorewithme.service.dto.rate;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.user.UserDto;
import ru.practicum.explorewithme.service.enums.Feedback;

@Data
public class EventFeedbackFullDto {
    private Long id;
    private EventFullDto event;
    private Feedback feedback;
    private UserDto user;
}

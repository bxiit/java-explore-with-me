package ru.practicum.explorewithme.service.dto.rate;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.user.UserShortDto;

@Data
public class EventFeedbackShortDto {
    private Long id;
    private EventShortDto event;
    private UserShortDto user;
}

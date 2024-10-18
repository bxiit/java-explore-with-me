package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;

import java.time.Instant;

@Data
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private Instant eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;
}

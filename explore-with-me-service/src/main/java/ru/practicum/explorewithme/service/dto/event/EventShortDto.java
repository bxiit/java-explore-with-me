package ru.practicum.explorewithme.service.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.util.InstantStringSerializer;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonSerialize(using = InstantStringSerializer.class)
    private Instant eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;
}

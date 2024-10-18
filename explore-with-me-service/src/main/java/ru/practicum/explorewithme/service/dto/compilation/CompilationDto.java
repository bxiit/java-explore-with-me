package ru.practicum.explorewithme.service.dto.compilation;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;

import java.util.List;

@Data
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}

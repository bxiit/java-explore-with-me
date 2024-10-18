package ru.practicum.explorewithme.service.dto.compilation;

import lombok.Data;
import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;
    private String title;
}

package ru.practicum.explorewithme.service.dto.compilation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;
import ru.practicum.explorewithme.service.validation.NotZeroLongId;

import java.util.List;

@Data
public class NewCompilationDto {
    @NotZeroLongId
    private List<Long> events;
    private Boolean pinned = false;

    @NotNull
    @NotOnlySpace
    @Size(min = 1, max = 50)
    private String title;
}

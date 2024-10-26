package ru.practicum.explorewithme.service.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;

@Data
public class NewCategoryDto {
    @NotNull
    @NotOnlySpace
    @Size(min = 1, max = 50)
    private String name;
}

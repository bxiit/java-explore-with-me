package ru.practicum.explorewithme.service.dto.category;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;

    @Size(min = 1, max = 50)
    private String name;
}

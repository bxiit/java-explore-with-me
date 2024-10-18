package ru.practicum.explorewithme.service.dto.category;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;

    //maxLength: 50
    //minLength: 1
    private String name;
}

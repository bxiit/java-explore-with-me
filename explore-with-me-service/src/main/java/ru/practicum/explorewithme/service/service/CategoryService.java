package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> get(int from, int size);

    CategoryDto get(Long catId);
}

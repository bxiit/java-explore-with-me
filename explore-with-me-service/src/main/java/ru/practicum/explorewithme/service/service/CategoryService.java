package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> get(int from, int size);

    CategoryDto get(Long catId);

    CategoryDto save(NewCategoryDto request);

    void delete(Long catId);

    CategoryDto edit(Long catId, CategoryDto request);
}

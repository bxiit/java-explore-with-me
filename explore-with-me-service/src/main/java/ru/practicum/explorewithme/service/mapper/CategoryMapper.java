package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.entity.Category;

@Mapper
public interface CategoryMapper {

    CategoryDto toDto(Category category);
}

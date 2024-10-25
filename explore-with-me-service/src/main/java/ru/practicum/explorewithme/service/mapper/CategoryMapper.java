package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.entity.Category;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toNewCategory(NewCategoryDto request);

    Category updateFields(@MappingTarget Category category, CategoryDto request);
}

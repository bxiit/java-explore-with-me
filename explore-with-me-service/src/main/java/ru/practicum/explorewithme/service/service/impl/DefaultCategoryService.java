package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.exception.ConflictException;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.CategoryMapper;
import ru.practicum.explorewithme.service.repository.CategoryRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    // for admin
    @Override
    @Transactional
    public CategoryDto save(NewCategoryDto request) {
        Category category = categoryMapper.toNewCategory(request);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    // for admin
    @Override
    @Transactional
    public void delete(Long catId) {
        checkForEmptyCategory(catId);
        int deleted = categoryRepository.deleteCategoryById(catId);
        if (deleted == 0) {
            throw new NotFoundException(Category.class, catId);
        }
    }

    // for admin
    @Override
    @Transactional
    public CategoryDto edit(Long catId, CategoryDto request) {
        Category category = categoryRepository.safeFetch(catId);
        category = categoryMapper.updateFields(category, request);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> get(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size)).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto get(Long catId) {
        Category category = fetchCategory(catId);
        return categoryMapper.toDto(category);
    }

    private Category fetchCategory(Long catId) {
        return categoryRepository.safeFetch(catId);
    }

    private void checkForEmptyCategory(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }
    }
}

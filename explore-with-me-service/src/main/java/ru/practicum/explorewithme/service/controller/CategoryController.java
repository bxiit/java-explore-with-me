package ru.practicum.explorewithme.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> get(
            @RequestParam("from") int from,
            @RequestParam("size") int size
    ) {
        return categoryService.get(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto get(@PathVariable("catId") Long catId) {
        return categoryService.get(catId);
    }
}

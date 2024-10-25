package ru.practicum.explorewithme.service.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.category.CategoryDto;
import ru.practicum.explorewithme.service.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.service.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Valid NewCategoryDto request) {
        return categoryService.save(request);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("catId") Long catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CategoryDto edit(@PathVariable("catId") Long catId, @RequestBody @Valid CategoryDto request) {
        return categoryService.edit(catId, request);
    }
}

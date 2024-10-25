package ru.practicum.explorewithme.service.repository;

import ru.practicum.explorewithme.service.entity.Category;

public interface CategoryRepository extends EwmRepository<Category> {
    @Override
    default Class<Category> entityClass() {
        return Category.class;
    };
}

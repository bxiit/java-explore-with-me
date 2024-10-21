package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

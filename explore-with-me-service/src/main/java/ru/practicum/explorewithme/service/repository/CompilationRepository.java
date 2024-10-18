package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.entity.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}

package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.Compilation;

@Repository
public interface CompilationRepository extends EwmRepository<Compilation>, JpaSpecificationExecutor<Compilation> {
    int deleteCompilationById(Long id);

    @Override
    default Class<Compilation> entityClass() {
        return Compilation.class;
    }
}

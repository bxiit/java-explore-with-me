package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends EwmRepository<Compilation>, JpaSpecificationExecutor<Compilation> {
    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);

    @Override
    default Class<Compilation> entityClass() {
        return Compilation.class;
    }
}

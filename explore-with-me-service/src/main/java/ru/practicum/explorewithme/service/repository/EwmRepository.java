package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.practicum.explorewithme.service.exception.NotFoundException;

@NoRepositoryBean
public interface EwmRepository<T> extends JpaRepository<T, Long> {

    default T safeFetch(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException(entityClass(), id));
    }

    Class<T> entityClass();
}

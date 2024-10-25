package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.enums.EventState;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends EwmRepository<Event>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndState(Long id, EventState state);

    boolean existsByCategoryId(Long categoryId);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long id);

    List<Event> findAllByCompilationId(Long compilationId);

    @Override
    default Class<Event> entityClass() {
        return Event.class;
    }
}

package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.enums.EventState;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends EwmRepository<Event>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("""
        select e
        from Event e
        left join fetch e.compilations
        where e.id in :eventIds
        """)
    List<Event> findAllById(Collection<Long> eventIds);

    Optional<Event> findByIdAndState(Long id, EventState state);

    boolean existsByCategoryId(Long categoryId);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long id);

    @Override
    default Class<Event> entityClass() {
        return Event.class;
    }
}

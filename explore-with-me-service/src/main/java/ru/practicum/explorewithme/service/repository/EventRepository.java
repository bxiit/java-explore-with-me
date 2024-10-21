package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.service.entity.Event;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    static Specification<Event> text(String text) {
        String pattern = "%" + text.toLowerCase() + "%";
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("annotation")), pattern),
                builder.like(builder.lower(root.get("description")), pattern)
        );
    }

    static Specification<Event> paid(Boolean paid) {
        return (root, query, builder) -> builder.equal(root.get("paid"), paid);
    }

    static Specification<Event> startAndEnd(Instant start, Instant end) {
        return (root, query, builder) -> builder.between(root.get("eventDate"), start, end);
    }

    static Specification<Event> defaultStartAndEnd() {
        return (root, query, builder) -> builder.greaterThan(root.get("eventDate"), Instant.now());
    }

    static Specification<Event> onlyAvailable() {
        return (root, query, builder) -> builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"));
    }

    static Specification<Event> categories(List<Long> categories) {
        return (root, query, builder) -> root.join("category").in(categories);
    }

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    @Query("""
            select e
            from Event e
            where e.category.id in :categoryIds
            """)
    List<Event> findAllByCategoryIds(Collection<Long> categoryIds);
}

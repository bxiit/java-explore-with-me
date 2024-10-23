package ru.practicum.explorewithme.service.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.service.entity.Event;

import java.time.Instant;
import java.util.List;

public class EventSpecifications {
    public static Specification<Event> text(String text) {
        String pattern = "%" + text.toLowerCase() + "%";
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("annotation")), pattern),
                builder.like(builder.lower(root.get("description")), pattern)
        );
    }

    public static Specification<Event> paid(Boolean paid) {
        return (root, query, builder) -> builder.equal(root.get("paid"), paid);
    }

    public static Specification<Event> startAndEnd(Instant start, Instant end) {
        return (root, query, builder) -> builder.between(root.get("eventDate"), start, end);
    }

    public static Specification<Event> defaultStartAndEnd() {
        return (root, query, builder) -> builder.greaterThan(root.get("eventDate"), Instant.now());
    }

    public static Specification<Event> onlyAvailable() {
        return (root, query, builder) -> builder.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"));
    }

    public static Specification<Event> categories(List<Long> categories) {
        return (root, query, builder) -> root.join("category").in(categories);
    }
}

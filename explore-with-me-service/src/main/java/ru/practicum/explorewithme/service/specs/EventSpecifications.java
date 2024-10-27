package ru.practicum.explorewithme.service.specs;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.entity.Event;

import java.time.Instant;
import java.util.Collection;

public class EventSpecifications {
    public static Specification<Event> text(String text) {
        String pattern = "%" + text.toLowerCase() + "%";
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get(Event.Fields.annotation)), pattern),
                builder.like(builder.lower(root.get(Event.Fields.description)), pattern)
        );
    }

    public static Specification<Event> paid(Boolean paid) {
        return (root, query, builder) -> builder.equal(root.get(Event.Fields.paid), paid);
    }

    public static Specification<Event> startAndEnd(Instant start, Instant end) {
        return (root, query, builder) -> builder.between(root.get(Event.Fields.eventDate), start, end);
    }

    public static Specification<Event> defaultStartAndEnd() {
        return (root, query, builder) -> builder.greaterThan(root.get(Event.Fields.eventDate), Instant.now());
    }

    public static Specification<Event> onlyAvailable() {
        return (root, query, builder) -> builder.greaterThan(root.get(Event.Fields.participantLimit), root.get(Event.Fields.confirmedRequests));
    }

    public static Specification<Event> categories(Collection<Long> categories) {
        return (root, query, builder) -> {
            Join<Category, Event> categoryJoin = root.join(Event.Fields.category);
            return categoryJoin.get("id").in(categories);
        };
    }

    public static Specification<Event> users(Collection<Long> users) {
        return (root, query, builder) -> {
            Join<Object, Object> userJoin = root.join(Event.Fields.initiator);
            return userJoin.get("id").in(users);
        };
    }
}

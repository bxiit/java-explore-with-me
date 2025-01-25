package ru.practicum.explorewithme.service.specs;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import ru.practicum.explorewithme.service.dto.event.GetEventsAdminRequest;
import ru.practicum.explorewithme.service.dto.event.GetEventsUserRequest;
import ru.practicum.explorewithme.service.entity.Category;
import ru.practicum.explorewithme.service.entity.Event;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static List<Specification<Event>> getAdminRequestSpecification(GetEventsAdminRequest request) {
        ArrayList<Specification<Event>> specifications = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getCategories())) {
            Specification<Event> specification = EventSpecifications.categories(request.getCategories());
            specifications.add(specification);
        }
        if (!CollectionUtils.isEmpty(request.getUsers())) {
            Specification<Event> specification = EventSpecifications.users(request.getUsers());
            specifications.add(specification);
        }
        if (request.getStart() != null && request.getEnd() != null) {
            Specification<Event> specification = EventSpecifications.startAndEnd(request.getStart(), request.getEnd());
            specifications.add(specification);
        } else {
            Specification<Event> specification = EventSpecifications.defaultStartAndEnd();
            specifications.add(specification);
        }
        return specifications;
    }

    public static List<Specification<Event>> getUserRequestSpecification(GetEventsUserRequest request) {
        ArrayList<Specification<Event>> specifications = new ArrayList<>();
        if (request.getText() != null) {
            Specification<Event> specification = EventSpecifications.text(request.getText());
            specifications.add(specification);
        }
        if (request.getPaid() != null) {
            Specification<Event> specification = EventSpecifications.paid(request.getPaid());
            specifications.add(specification);
        }
        if (request.getStart() != null && request.getEnd() != null) {
            Specification<Event> specification = EventSpecifications.startAndEnd(request.getStart(), request.getEnd());
            specifications.add(specification);
        } else {
            Specification<Event> specification = EventSpecifications.defaultStartAndEnd();
            specifications.add(specification);
        }
        if (request.getOnlyAvailable()) {
            Specification<Event> specification = EventSpecifications.onlyAvailable();
            specifications.add(specification);
        }
        if (!CollectionUtils.isEmpty(request.getCategories())) {
            Specification<Event> specification = EventSpecifications.categories(request.getCategories());
            specifications.add(specification);
        }
        return specifications;
    }
}

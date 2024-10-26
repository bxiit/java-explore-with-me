package ru.practicum.explorewithme.service.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.service.entity.User;

import java.util.Collection;

public class UserSpecifications {
    public static Specification<User> users(Collection<Long> ids) {
        return (root, query, builder) -> root.get("id").in(ids);
    }
}

package ru.practicum.explorewithme.service.specs;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.explorewithme.service.entity.Compilation;

public class CompilationSpecification {
    public static Specification<Compilation> pinned(Boolean pinned) {
        return (root, query, cb) -> cb.equal(root.get(Compilation.Fields.pinned), pinned);
    }
}

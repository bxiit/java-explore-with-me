package ru.practicum.explorewithme.service.enums;

import org.springframework.data.domain.Sort;

public enum SortBy implements SortParameter {
    EVENT_DATE {
        @Override
        public Sort getSort() {
            return Sort.by("eventDate");
        }
    },
    VIEWS {
        @Override
        public Sort getSort() {
            return Sort.by("views");
        }
    };
}

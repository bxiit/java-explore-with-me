package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.service.enums.SortBy;

import java.time.Instant;
import java.util.List;

@Data
public class GetEventsUserRequest {
    private String text;
    private List<@Positive @NotNull Long> categories;
    private Boolean paid;
    private Instant start;
    private Instant end;
    private Boolean onlyAvailable = false;
    private SortBy sort;
    private Integer from = 0;
    private Integer size = 10;

    public Pageable getPageable() {
        return getSort() == null ? PageRequest.of(getFrom(), getSize())
                : PageRequest.of(getFrom(), getSize(), getSort().getSort());
    }
}

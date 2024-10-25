package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.service.enums.EventState;
import ru.practicum.explorewithme.service.validation.NotZeroLongId;

import java.time.Instant;
import java.util.List;

@Data
public class GetEventsAdminRequest {
    @NotZeroLongId
    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private Instant start;
    private Instant end;
    private Integer from = 0;
    private Integer size = 10;

    public Pageable getPageable() {
        return PageRequest.of(getFrom(), getSize());
    }
}

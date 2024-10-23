package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.service.enums.SortBy;

import java.time.Instant;
import java.util.List;

@Data
public class GetEventsRequest {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private Instant start;
    private Instant end;
    private boolean onlyAvailable = false;
    private SortBy sort;
    private int from;
    private int size = 10;

    public Pageable getPageable(){
        return PageRequest.of(getFrom(), getSize(), getSort().getSort());
    }
}

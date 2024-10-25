package ru.practicum.explorewithme.service.dto.user;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.service.validation.NotZeroLongId;

import java.util.List;

@Data
public class GetUsersAdminRequest {
    @NotZeroLongId
    private List<Long> ids;
    private Integer from = 0;
    private Integer size = 10;

    public Pageable getPageable() {
        return PageRequest.of(getFrom(), getSize());
    }
}

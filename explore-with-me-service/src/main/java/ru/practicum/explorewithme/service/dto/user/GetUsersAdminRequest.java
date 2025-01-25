package ru.practicum.explorewithme.service.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class GetUsersAdminRequest {
    private List<@Positive @NotNull Long> ids;
    private Integer from = 0;
    private Integer size = 10;

    public Pageable getPageable() {
        return PageRequest.of(getFrom(), getSize());
    }
}

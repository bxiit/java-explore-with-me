package ru.practicum.explorewithme.service.dto.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.explorewithme.service.enums.EventRequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<@Positive @NotNull Long> requestIds;
    private EventRequestStatus status;

    public boolean isReject() {
        return getStatus().equals(EventRequestStatus.REJECTED);
    }
}

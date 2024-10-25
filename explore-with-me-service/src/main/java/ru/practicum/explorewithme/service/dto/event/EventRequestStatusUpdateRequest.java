package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.service.enums.EventRequestStatus;
import ru.practicum.explorewithme.service.validation.NotZeroLongId;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    @NotZeroLongId
    private List<Long> requestIds;
    private EventRequestStatus status;
}

package ru.practicum.explorewithme.service.dto.event;

import lombok.Data;
import ru.practicum.explorewithme.service.dto.user.ParticipationRequestDto;

import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}

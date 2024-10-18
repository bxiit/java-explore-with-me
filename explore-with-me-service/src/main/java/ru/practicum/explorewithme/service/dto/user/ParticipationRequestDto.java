package ru.practicum.explorewithme.service.dto.user;

import lombok.Data;
import java.time.Instant;

@Data
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private Instant created;
    private String status;
}

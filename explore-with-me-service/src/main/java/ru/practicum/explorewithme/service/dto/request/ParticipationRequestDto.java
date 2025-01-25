package ru.practicum.explorewithme.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private Instant created;
    private ParticipationStatus status;
}

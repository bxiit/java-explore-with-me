package ru.practicum.explorewithme.service.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;
import ru.practicum.explorewithme.service.util.InstantStringSerializer;

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
    @JsonSerialize(using = InstantStringSerializer.class)
    private Instant created;
    private ParticipationStatus status;
}

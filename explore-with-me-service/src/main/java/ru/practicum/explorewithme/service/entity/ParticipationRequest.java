package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParticipationRequest extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private Event event;
    @OneToOne(fetch = FetchType.LAZY)
    private User requester;
    private Instant created;
    private ParticipationStatus status;
}

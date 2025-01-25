package ru.practicum.explorewithme.service.repository;

import ru.practicum.explorewithme.service.entity.ParticipationRequest;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends EwmRepository<ParticipationRequest> {
    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Integer countAllByEventIdAndStatusIn(Long eventId, Collection<ParticipationStatus> status);

    @Override
    default Class<ParticipationRequest> entityClass() {
        return ParticipationRequest.class;
    }
}

package ru.practicum.explorewithme.service.repository;

import ru.practicum.explorewithme.service.entity.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends EwmRepository<ParticipationRequest> {
    Integer countAllByEventId(Long eventId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    @Override
    default Class<ParticipationRequest> entityClass() {
        return ParticipationRequest.class;
    }
}

package ru.practicum.explorewithme.service.repository;

import ru.practicum.explorewithme.service.entity.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends EwmRepository<ParticipationRequest> {
    Integer countAllByEventId(Long eventId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByRequesterIdAndEventId(Long id, Long eventId);

    @Override
    default Class<ParticipationRequest> entityClass() {
        return ParticipationRequest.class;
    }
}

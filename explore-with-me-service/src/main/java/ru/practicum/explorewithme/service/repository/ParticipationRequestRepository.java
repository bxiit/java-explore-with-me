package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.entity.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Integer countAllByEventId(Long eventId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByRequesterIdAndEventId(Long id, Long eventId);
}

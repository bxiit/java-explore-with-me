package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.service.entity.ParticipationRequest;
import ru.practicum.explorewithme.service.enums.ParticipationStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Integer countAllByEventIdAndStatusIn(Long eventId, Collection<ParticipationStatus> status);
}

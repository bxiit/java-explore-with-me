package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<ru.practicum.explorewithme.service.entity.EventFeedback, Long> {
    List<ru.practicum.explorewithme.service.entity.EventFeedback> findAllByEventId(Long eventId, Pageable pageable);

    List<ru.practicum.explorewithme.service.entity.EventFeedback> findAllByEventId(Long eventId);

    Optional<ru.practicum.explorewithme.service.entity.EventFeedback> findByEventIdAndUserId(Long event_id, Long user_id);
}

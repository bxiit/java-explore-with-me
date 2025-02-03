package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.EventFeedback;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<EventFeedback, Long> {
    List<EventFeedback> findAllByEventId(Long eventId, Pageable pageable);

    List<EventFeedback> findAllByEventId(Long eventId);

    Optional<EventFeedback> findByEventIdAndUserId(Long eventId, Long userId);
}

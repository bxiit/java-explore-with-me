package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.Rate;

import java.util.List;
import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByEventId(Long eventId, Pageable pageable);

    List<Rate> findAllByEventId(Long eventId);

    Optional<Rate> findByEventIdAndUserId(Long eventId, Long userId);

    List<Rate> findAllByEventIdIn(List<Long> eventsIds);
}

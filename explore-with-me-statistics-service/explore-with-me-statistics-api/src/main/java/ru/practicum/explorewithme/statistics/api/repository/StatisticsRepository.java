package ru.practicum.explorewithme.statistics.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.statistics.api.entity.Endpoint;

import java.time.Instant;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Endpoint, Long> {
    @Query(value = """
            select e.app as app,
            e.uri as uri,
            count(e.id) as hits
            from endpoint e
            where (e.timestamp between :start and :end)
            and (e.uri in :uris)
            group by e.app, e.uri
            having (:unique is false or count(e.id) = 1)
            order by hits desc
            """, nativeQuery = true)
    List<ViewStatsProj> findAllWithUri(Instant start, Instant end, String[] uris, boolean unique);

    @Query(value = """
            select e.app as app,
            e.uri as uri,
            count(e.id) as hits
            from Endpoint e
            where (e.timestamp between :start and :end)
            group by e.app, e.uri
            having (:unique is false or count(e.id) = 1)
            order by hits desc
            """)
    List<ViewStatsProj> findAllWithoutUri(Instant start, Instant end, boolean unique);
}

package ru.practicum.explorewithme.statistics.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.statistics.api.entity.Endpoint;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Endpoint, Long> {
    @Query(value = """
            select e.app as app,
            e.uri as uri,
            count(e.id) as hits
            from Endpoint e
            where (e.timestamp between ?1 and ?2)
            and e.uri in ?3
            group by e.app, e.uri
            having (?4 is false or count(e.id) = 1)
            """)
    List<ViewStatsProj> findAllWithUri(Instant start, Instant end, Collection<String> uris, boolean unique);

    @Query(value = """
            select e.app as app,
            e.uri as uri,
            count(e.id) as hits
            from Endpoint e
            where (e.timestamp between ?1 and ?2)
            group by e.app, e.uri
            having (?3 is false or count(e.id) = 1)
            """)
    List<ViewStatsProj> findAllWithoutUri(Instant start, Instant end, boolean unique);
}

package ru.practicum.explorewithme.statistics.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.statistics.api.entity.Endpoint;
import ru.practicum.explorewithme.statistics.contract.model.ViewStats;

import java.time.Instant;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Endpoint, Long> {
    @Query("""
            select new ru.practicum.explorewithme.statistics.contract.model.ViewStats(eh.app, eh.uri, count(eh.ip)) from Endpoint eh
            where eh.timestamp between ?1 and ?2
            group by eh.app, eh.uri
            order by count(eh.ip) desc
            """)
    List<ViewStats> findByStartAndEnd(Instant start, Instant end);

    @Query("""
            select new ru.practicum.explorewithme.statistics.contract.model.ViewStats(eh.app, eh.uri, count(eh.ip)) from Endpoint eh
            where eh.timestamp between ?1 and ?2 and eh.uri in ?3
            group by eh.app, eh.uri
            order by count(eh.ip) desc
            """)
    List<ViewStats> findWithUris(Instant start, Instant end, String[] uris);

    @Query("""
            select new ru.practicum.explorewithme.statistics.contract.model.ViewStats(eh.app, eh.uri, count(distinct eh.ip)) from Endpoint eh
            where eh.timestamp between ?1 and ?2
            group by eh.app, eh.uri
            order by count(distinct eh.ip) desc
            """)
    List<ViewStats> findUniqueIp(Instant start, Instant end);

    @Query("""
            select new ru.practicum.explorewithme.statistics.contract.model.ViewStats(eh.app, eh.uri, count(distinct eh.ip)) from Endpoint eh
            where eh.timestamp between ?1 and ?2 and eh.uri in ?3
            group by eh.app, eh.uri
            order by count(distinct eh.ip) desc
            """)
    List<ViewStats> findUniqueIpWithUris(Instant start, Instant end, String[] uris);
}

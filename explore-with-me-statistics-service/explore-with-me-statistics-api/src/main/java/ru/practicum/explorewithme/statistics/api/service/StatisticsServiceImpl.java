package ru.practicum.explorewithme.statistics.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.api.entity.Endpoint;
import ru.practicum.explorewithme.statistics.api.mapper.EndpointMapper;
import ru.practicum.explorewithme.statistics.api.repository.StatisticsRepository;
import ru.practicum.explorewithme.statistics.contract.model.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.model.ViewStats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    public void save(EndpointHit request) {
        log.info("Save endpoint hit for app:{}", request.getApp());
        Endpoint hit = EndpointMapper.mapToEntity(request);
        statisticsRepository.save(hit);
    }

    @Override
    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris.isEmpty()) {
            return statisticsRepository.findAllWithoutUri(toInstant(start), toInstant(end), unique).stream()
                    .map(vs -> new ViewStats(vs.getApp(), vs.getUri(), vs.getHits()))
                    .toList();
        } else {
            return statisticsRepository.findAllWithUri(toInstant(start), toInstant(end), uris, unique).stream()
                    .map(vs -> new ViewStats(vs.getApp(), vs.getUri(), vs.getHits()))
                    .toList();
        }
    }

    private Instant toInstant(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.UTC);
    }
}

package ru.practicum.explorewithme.statistics.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.api.dto.GetViewStatsRequest;
import ru.practicum.explorewithme.statistics.api.entity.Endpoint;
import ru.practicum.explorewithme.statistics.api.mapper.EndpointMapper;
import ru.practicum.explorewithme.statistics.api.repository.StatisticsRepository;
import ru.practicum.explorewithme.statistics.contract.model.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.model.ViewStats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatisticsRepository statisticsRepository;

    @Override
    public void save(EndpointHit request) {
        log.info("Save endpoint hit for app:{}", request.getApp());
        Endpoint hit = EndpointMapper.mapToEntity(request, parseStringToInstant(request.getTimestamp()));
        statisticsRepository.save(hit);
    }

    @Override
    public List<ViewStats> get(GetViewStatsRequest request) {
        if (request.getUris().isEmpty()) {
            return statisticsRepository.findAllWithoutUri(toInstant(request.getStart()), toInstant(request.getEnd()), request.isUnique()).stream()
                    .map(vs -> new ViewStats(vs.getApp(), vs.getUri(), vs.getHits()))
                    .toList();
        } else {
            return statisticsRepository.findAllWithUri(
                            toInstant(request.getStart()),
                            toInstant(request.getEnd()),
                            addPercentSymbolForEach(request.getUris()).toArray(String[]::new),
                            request.isUnique()
                    ).stream()
                    .map(vs -> new ViewStats(vs.getApp(), vs.getUri(), vs.getHits()))
                    .toList();
        }
    }

    private List<String> addPercentSymbolForEach(Collection<String> uris) {
        return uris.stream().map(uri -> "%" + uri + "%").toList();
    }

    private Instant toInstant(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.UTC);
    }

    private Instant parseStringToInstant(String timestamp) {
        return toInstant(LocalDateTime.parse(timestamp, DATE_TIME_FORMATTER));
    }
}

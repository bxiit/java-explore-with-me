package ru.practicum.explorewithme.statistics.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.explorewithme.statistics.api.dto.GetViewStatsRequest;
import ru.practicum.explorewithme.statistics.api.entity.Endpoint;
import ru.practicum.explorewithme.statistics.api.mapper.EndpointMapper;
import ru.practicum.explorewithme.statistics.api.repository.StatisticsRepository;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public List<ViewStats> get(GetViewStatsRequest request) throws MethodArgumentNotValidException {
        List<ViewStats> stats = new ArrayList<>();
        boolean unique = request.getUnique() != null && request.getUnique();
        validateStartAndEndDate(request);

        if (!unique && request.getUris() == null) {
            stats = statisticsRepository.findByStartAndEnd(toInstant(request.getStart()), toInstant(request.getEnd()));
        }
        if (!unique && request.getUris() != null) {
            stats = statisticsRepository.findWithUris(toInstant(request.getStart()), toInstant(request.getEnd()), request.getUris());
        }
        if (unique && request.getUris() == null) {
            stats = statisticsRepository.findUniqueIp(toInstant(request.getStart()), toInstant(request.getEnd()));
        }
        if (unique && request.getUris() != null) {
            stats = statisticsRepository.findUniqueIpWithUris(toInstant(request.getStart()), toInstant(request.getEnd()), request.getUris());
        }
        return stats;
    }

    private void validateStartAndEndDate(GetViewStatsRequest request) throws MethodArgumentNotValidException {
        if (request.getStart().isAfter(request.getEnd())) {
            throw new MethodArgumentNotValidException(null, new BeanPropertyBindingResult(new Object(), "request"));
        }
    }

    private Instant toInstant(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.UTC);
    }

    private Instant parseStringToInstant(String timestamp) {
        return toInstant(LocalDateTime.parse(timestamp, DATE_TIME_FORMATTER));
    }
}

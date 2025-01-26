package ru.practicum.explorewithme.statistics.api.service;

import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.explorewithme.statistics.api.dto.GetViewStatsRequest;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.util.List;

public interface StatisticsService {
    void save(EndpointHit hit);

    List<ViewStats> get(GetViewStatsRequest request) throws MethodArgumentNotValidException;
}

package ru.practicum.explorewithme.statistics.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.statistics.api.dto.GetViewStatsRequest;
import ru.practicum.explorewithme.statistics.api.service.StatisticsService;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody EndpointHit hit) {
        statisticsService.save(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(GetViewStatsRequest request) {
        return statisticsService.get(request);
    }
}

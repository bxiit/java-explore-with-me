package ru.practicum.explorewithme.statistics.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.statistics.contract.AppConstants;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatisticsClient extends BaseClient {

    private static final ParameterizedTypeReference<List<ViewStats>> VIEW_STATS_TYPE =
            new ParameterizedTypeReference<>() {};

    public StatisticsClient(@Value("${statistics.server.url}") String url, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public void hit(HttpServletRequest requestInfo) {
        Runnable saveToStatisticsService = () -> {
            log.info("Save to statistics service");
            ResponseEntity<Object> response = saveHit(
                    new EndpointHit(
                            "ewm-main-service", requestInfo.getRequestURI(), requestInfo.getRemoteAddr(),
                            AppConstants.DATE_TIME_FORMATTER.format(LocalDateTime.now())
                    )
            );
            log.info("Response from statistics service {}", response.toString());
        };
        Thread thread = new Thread(saveToStatisticsService);
        thread.start();
    }

    public ResponseEntity<Object> saveHit(EndpointHit hit) {
        return post("/hit", hit);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        Map<String, Object> params = Map.of("start", start, "end", end, "unique", unique, "uris", uris);
        return rest.exchange(
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET, new HttpEntity<>(defaultHeaders()),
                VIEW_STATS_TYPE, params).getBody();
    }
}

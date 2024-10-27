package ru.practicum.explorewithme.statistics.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;
import ru.practicum.explorewithme.statistics.contract.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

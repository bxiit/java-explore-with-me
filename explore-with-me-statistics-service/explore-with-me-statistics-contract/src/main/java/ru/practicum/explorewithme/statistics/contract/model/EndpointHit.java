package ru.practicum.explorewithme.statistics.contract.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
    private Instant timestamp;
}

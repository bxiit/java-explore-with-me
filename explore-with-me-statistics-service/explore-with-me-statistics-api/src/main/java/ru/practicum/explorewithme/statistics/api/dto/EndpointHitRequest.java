package ru.practicum.explorewithme.statistics.api.dto;

import lombok.Data;

@Data
public class EndpointHitRequest {
    private String app;
    private String uri;
    private String ip;
}

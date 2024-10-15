package ru.practicum.explorewithme.statistics.api.mapper;

import ru.practicum.explorewithme.statistics.api.entity.Endpoint;
import ru.practicum.explorewithme.statistics.contract.model.EndpointHit;

import java.time.Instant;

public class EndpointMapper {

    public static Endpoint
    mapToEntity(EndpointHit request) {
        return new Endpoint(null, request.getApp(), request.getUri(), request.getIp(), Instant.now());
    }
}

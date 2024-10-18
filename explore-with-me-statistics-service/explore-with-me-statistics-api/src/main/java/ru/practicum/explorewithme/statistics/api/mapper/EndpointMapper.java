package ru.practicum.explorewithme.statistics.api.mapper;

import ru.practicum.explorewithme.statistics.api.entity.Endpoint;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;

import java.time.Instant;

public class EndpointMapper {

    public static Endpoint mapToEntity(EndpointHit request, Instant timestamp) {
        return new Endpoint(null, request.getApp(), request.getUri(), request.getIp(), timestamp);
    }
}

package ru.practicum.explorewithme.service.service;

import io.debezium.data.Envelope;

import java.util.Map;

public interface UserHistoryService {
    void replicateData(Map<String, Object> payload, Envelope.Operation operation);
}

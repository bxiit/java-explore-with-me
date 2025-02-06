package ru.practicum.explorewithme.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.data.Envelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.service.UserHistoryService;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserHistoryService implements UserHistoryService {

    private final ObjectMapper objectMapper;

    @Override
    public void replicateData(Map<String, Object> userData, Envelope.Operation operation) {
        User user = objectMapper.convertValue(userData, User.class);

        if (operation == Envelope.Operation.DELETE) {
            log.info("User with id: {} deleted", user.getId());
        } else {
            log.info("User: {}", user);
        }
    }
}

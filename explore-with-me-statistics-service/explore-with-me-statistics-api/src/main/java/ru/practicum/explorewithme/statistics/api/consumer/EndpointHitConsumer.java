package ru.practicum.explorewithme.statistics.api.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.statistics.api.service.StatisticsService;
import ru.practicum.explorewithme.statistics.contract.dto.EndpointHit;

@Component
@RequiredArgsConstructor
public class EndpointHitConsumer {

    private final StatisticsService statisticsService;

    @KafkaListener(topics = "statistics")
    void listen(EndpointHit record, Acknowledgment acknowledgment) {
        try {
            statisticsService.save(record);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

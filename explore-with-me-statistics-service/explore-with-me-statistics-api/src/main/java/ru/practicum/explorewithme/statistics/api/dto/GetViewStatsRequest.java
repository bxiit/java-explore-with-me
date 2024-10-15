package ru.practicum.explorewithme.statistics.api.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetViewStatsRequest {
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm")
    LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm")
    LocalDateTime end;
    List<String> uris = new ArrayList<>();
    boolean unique;
}

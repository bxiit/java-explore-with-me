package ru.practicum.explorewithme.service.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private Instant timestamp;
}

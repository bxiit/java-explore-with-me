package ru.practicum.explorewithme.service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static ru.practicum.explorewithme.service.util.AppConstants.DATE_TIME_FORMATTER;

@JsonComponent
public class InstantStringDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return parseStringToInstant(p.getValueAsString());
    }

    private Instant parseStringToInstant(String timestamp) {
        return toInstant(LocalDateTime.parse(timestamp, DATE_TIME_FORMATTER));
    }

    private Instant toInstant(LocalDateTime ldt) {
        return ldt.toInstant(ZoneOffset.UTC);
    }
}

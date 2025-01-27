package ru.practicum.explorewithme.service.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Instant;

import static ru.practicum.explorewithme.service.util.AppConstants.DATE_TIME_FORMATTER;

@JsonComponent
public class InstantStringSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) gen.writeNull();
        gen.writeObject(format(value));
    }

    private String format(Instant value) {
        return DATE_TIME_FORMATTER.format(value);
    }
}

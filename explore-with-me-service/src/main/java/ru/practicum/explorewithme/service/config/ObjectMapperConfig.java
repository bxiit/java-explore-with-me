package ru.practicum.explorewithme.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.practicum.explorewithme.service.util.InstantStringDeserializer;
import ru.practicum.explorewithme.service.util.InstantStringSerializer;

import java.time.Instant;

@Configuration
public class ObjectMapperConfig {

    @Autowired
    public void objectMapper(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, new InstantStringDeserializer());
        module.addSerializer(Instant.class, new InstantStringSerializer());
        objectMapper.registerModule(module);
    }
}

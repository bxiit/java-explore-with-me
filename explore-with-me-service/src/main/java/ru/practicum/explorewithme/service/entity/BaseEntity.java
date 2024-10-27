package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    protected Instant created;

    @PrePersist
    private void prePersist() {
        this.created = Instant.now();
    }
}

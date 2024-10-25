package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant created;

    @PrePersist
    private void prePersist() {
        this.created = Instant.now();
    }
}

package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Location {
    @Column(name = "latitude")
    private Float lat;
    @Column(name = "longitude")
    private Float lon;
}

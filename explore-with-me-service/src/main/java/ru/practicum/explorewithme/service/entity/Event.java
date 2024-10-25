package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.dto.Location;
import ru.practicum.explorewithme.service.enums.EventState;

import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {

    @Column(length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Integer confirmedRequests;

    @Column(length = 7000)
    private String description;

    private Instant eventDate;

    @OneToOne(fetch = FetchType.LAZY)
    private User initiator;

    @Embedded
    private Location location;

    private Boolean paid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Compilation compilation;

    private Integer participantLimit;

    private Instant publishedOn;

    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(length = 120)
    private String title;

    private Long views;
}

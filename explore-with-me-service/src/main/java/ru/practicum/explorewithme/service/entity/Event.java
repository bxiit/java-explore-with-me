package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import ru.practicum.explorewithme.service.enums.EventState;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@EqualsAndHashCode(of = "id", callSuper = false)
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Integer confirmedRequests;

    @Column(length = 7000)
    private String description;

    private Instant eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;

    @Embedded
    private Location location;

    private Boolean paid;

    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations = new HashSet<>();

    private Integer participantLimit;

    private Instant publishedOn;

    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    @Enumerated(EnumType.STRING)
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private EventState state;

    @Column(length = 120)
    private String title;

    private Long views;
}

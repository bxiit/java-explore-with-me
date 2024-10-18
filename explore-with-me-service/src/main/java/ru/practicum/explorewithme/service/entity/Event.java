package ru.practicum.explorewithme.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.service.dto.Location;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Event extends BaseEntity {

    private String annotation;

    private Category category;

    private String description;

    private Instant eventDate;

    private User initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Instant publishedOn;

    private Boolean requestModeration; // Нужна ли пре-модерация заявок на участие

    private String state;

    private String title;

    private Long views;
}

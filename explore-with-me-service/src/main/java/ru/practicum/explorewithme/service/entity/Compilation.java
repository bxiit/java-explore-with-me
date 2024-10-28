package ru.practicum.explorewithme.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Compilation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean pinned;

    private String title;

    @ManyToMany
    @JoinTable(name = "compilation_event")
    @Builder.Default
    private Set<Event> events = new HashSet<>();

    public void addEvents(Collection<Event> events) {
        events.forEach(this::addEvent);
    }

    public void addEvent(Event event) {
        this.getEvents().add(event);
        event.getCompilations().add(this);
    }
}

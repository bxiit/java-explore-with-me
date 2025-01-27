package ru.practicum.explorewithme.service.enums;

import ru.practicum.explorewithme.service.entity.Event;

public interface UpdateEventAction {
    void updateEventState(Event event);
}

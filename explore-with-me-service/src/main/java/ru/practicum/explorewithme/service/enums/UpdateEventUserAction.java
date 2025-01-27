package ru.practicum.explorewithme.service.enums;

import ru.practicum.explorewithme.service.entity.Event;

public enum UpdateEventUserAction implements UpdateEventAction {
    SEND_TO_REVIEW {
        @Override
        public void updateEventState(Event event) {
            event.setState(EventState.PENDING);
        }
    },
    CANCEL_REVIEW {
        @Override
        public void updateEventState(Event event) {
            event.setState(EventState.CANCELED);
        }
    }
}

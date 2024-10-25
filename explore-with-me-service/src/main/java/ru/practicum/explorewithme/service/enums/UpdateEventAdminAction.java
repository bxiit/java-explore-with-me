package ru.practicum.explorewithme.service.enums;

import ru.practicum.explorewithme.service.entity.Event;

public enum UpdateEventAdminAction implements UpdateEventAction {
    PUBLISH_EVENT {
        @Override
        public void updateEventState(Event event) {
            event.setState(EventState.PUBLISHED);
        }
    },
    REJECT_EVENT {
        @Override
        public void updateEventState(Event event) {
            event.setState(EventState.CANCELED);
        }
    }
}

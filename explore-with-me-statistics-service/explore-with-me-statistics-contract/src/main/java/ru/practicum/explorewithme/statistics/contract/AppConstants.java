package ru.practicum.explorewithme.statistics.contract;

import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;

public class AppConstants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(UTC);
}

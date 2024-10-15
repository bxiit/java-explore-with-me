package ru.practicum.explorewithme.statistics.contract.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Integer hits;
}

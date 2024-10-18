package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> get(Boolean pinned, int from, int size);

    CompilationDto get(Long compId);
}

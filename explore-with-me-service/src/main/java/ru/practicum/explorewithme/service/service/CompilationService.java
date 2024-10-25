package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> get(Boolean pinned, int from, int size);

    CompilationDto get(Long compId);

    CompilationDto saveNewCompilation(NewCompilationDto request);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request);
}

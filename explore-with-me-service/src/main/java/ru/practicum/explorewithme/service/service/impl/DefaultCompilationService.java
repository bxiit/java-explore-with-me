package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.CompilationMapper;
import ru.practicum.explorewithme.service.repository.CompilationRepository;
import ru.practicum.explorewithme.service.service.CompilationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCompilationService implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> get(Boolean pinned, int from, int size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size)).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CompilationDto get(Long compId) {
        return compilationRepository.findById(compId)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }
}

package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.entity.Compilation;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.mapper.CompilationMapper;
import ru.practicum.explorewithme.service.repository.CompilationRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.service.CompilationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCompilationService implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper mapper;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> get(Boolean pinned, int from, int size) {
        return compilationRepository.findAllByPinned(pinned, PageRequest.of(from, size)).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CompilationDto get(Long compId) {
        Compilation compilation = fetchCompilation(compId);
        return mapper.toDto(compilation);
    }

    @Override
    @Transactional
    public CompilationDto saveNewCompilation(NewCompilationDto request) {
        Compilation compilation = mapper.toNewCompilation(request);
        List<Event> compilationEvents = eventRepository.findAllById(request.getEvents());
        compilationEvents.forEach(event -> event.setCompilation(compilation));
        compilationRepository.save(compilation);
        return mapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        checkForExistence(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.safeFetch(compId);
        compilation = mapper.updateFields(compilation, request);
        return mapper.toDto(compilation);
    }

    private void checkForExistence(Long compId) {
        compilationRepository.existenceCheck(compId);
    }

    private Compilation fetchCompilation(Long compId) {
        return compilationRepository.safeFetch(compId);
    }
}

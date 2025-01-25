package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.entity.Compilation;
import ru.practicum.explorewithme.service.entity.Event;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.CompilationMapper;
import ru.practicum.explorewithme.service.repository.CompilationRepository;
import ru.practicum.explorewithme.service.repository.EventRepository;
import ru.practicum.explorewithme.service.service.CompilationService;
import ru.practicum.explorewithme.service.specs.CompilationSpecification;

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
        Specification<Compilation> specification = null;
        if (pinned != null) {
            specification = CompilationSpecification.pinned(pinned);
        }
        return compilationRepository.findAll(specification, PageRequest.of(from, size)).stream()
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
        log.debug("Saving new compilation with events: {} and title: {}", request.getEvents(), request.getTitle());
        Compilation compilation = mapper.toNewCompilation(request);
        List<Event> compilationEvents = eventRepository.findAllById(request.getEvents());
        compilationEvents.forEach(compilation::addEvent);
        compilationRepository.save(compilation);
        return mapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        int deleted = compilationRepository.deleteCompilationById(compId);
        if (deleted == 0) {
            throw new NotFoundException(Compilation.class, compId);
        }
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        log.debug("Updating compilation with id: {}, request: {}", compId, request);
        Compilation compilation = compilationRepository.safeFetch(compId);
        if (!CollectionUtils.isEmpty(request.getEvents())) {
            List<Event> events = eventRepository.findAllById(request.getEvents());
            compilation.addEvents(events);
        }
        compilation = mapper.updateFields(compilation, request);
        return mapper.toDto(compilation);
    }

    private Compilation fetchCompilation(Long compId) {
        return compilationRepository.safeFetch(compId);
    }
}

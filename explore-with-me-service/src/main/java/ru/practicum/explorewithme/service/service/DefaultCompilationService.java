package ru.practicum.explorewithme.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.service.repository.CompilationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCompilationService implements CompilationService {
    private final CompilationRepository compilationRepository;
}

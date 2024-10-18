package ru.practicum.explorewithme.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;
}

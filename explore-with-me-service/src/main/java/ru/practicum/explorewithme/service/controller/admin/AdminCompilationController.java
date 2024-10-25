package ru.practicum.explorewithme.service.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    public final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveNewCompilation(@RequestBody @Valid NewCompilationDto request) {
        return compilationService.saveNewCompilation(request);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable("compId") Long compId,
            @RequestBody @Valid UpdateCompilationRequest request
    ) {
        return compilationService.updateCompilation(compId, request);
    }
}

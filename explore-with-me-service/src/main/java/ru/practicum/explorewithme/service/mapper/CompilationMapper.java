package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.entity.Compilation;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);

    Compilation toNewCompilation(NewCompilationDto request);

    Compilation updateFields(@MappingTarget Compilation compilation, UpdateCompilationRequest request);
}

package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.entity.Compilation;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation toNewCompilation(NewCompilationDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    Compilation updateFields(@MappingTarget Compilation compilation, UpdateCompilationRequest request);
}

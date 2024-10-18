package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.service.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.entity.Compilation;

@Mapper(uses = {EventMapper.class})
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation);
}

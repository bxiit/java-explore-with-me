package ru.practicum.explorewithme.service.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.service.dto.user.NewUserRequest;
import ru.practicum.explorewithme.service.dto.user.UserDto;
import ru.practicum.explorewithme.service.dto.user.UserShortDto;
import ru.practicum.explorewithme.service.entity.User;

@Mapper
public interface UserMapper {
    UserShortDto toShortDto(User user);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    User toNewUser(NewUserRequest request);
}

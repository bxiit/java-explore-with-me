package ru.practicum.explorewithme.service.service;

import ru.practicum.explorewithme.service.dto.user.GetUsersAdminRequest;
import ru.practicum.explorewithme.service.dto.user.NewUserRequest;
import ru.practicum.explorewithme.service.dto.user.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> get(GetUsersAdminRequest request);

    UserDto save(NewUserRequest request);

    void deleteUser(Long userId);
}

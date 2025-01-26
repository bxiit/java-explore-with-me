package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.explorewithme.service.dto.user.GetUsersAdminRequest;
import ru.practicum.explorewithme.service.dto.user.NewUserRequest;
import ru.practicum.explorewithme.service.dto.user.UserDto;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.UserMapper;
import ru.practicum.explorewithme.service.repository.UserRepository;
import ru.practicum.explorewithme.service.service.UserService;
import ru.practicum.explorewithme.service.specs.UserSpecifications;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> get(GetUsersAdminRequest request) {
        List<Specification<User>> specifications = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getIds())) {
            specifications.add(UserSpecifications.users(request.getIds()));
        }
        return userRepository.findAll(Specification.allOf(specifications), request.getPageable()).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest request) {
        User user = userMapper.toNewUser(request);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        int deleted = userRepository.deleteUserById(userId);
        if (deleted == 0) {
            throw new NotFoundException(User.class, userId);
        }
    }
}

package ru.practicum.explorewithme.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.explorewithme.service.dto.event.EventFullDto;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.service.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.service.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.dto.event.NewEventDto;
import ru.practicum.explorewithme.service.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.dto.user.GetUsersAdminRequest;
import ru.practicum.explorewithme.service.dto.user.NewUserRequest;
import ru.practicum.explorewithme.service.dto.user.UserDto;
import ru.practicum.explorewithme.service.entity.User;
import ru.practicum.explorewithme.service.exception.NotFoundException;
import ru.practicum.explorewithme.service.mapper.UserMapper;
import ru.practicum.explorewithme.service.repository.UserRepository;
import ru.practicum.explorewithme.service.service.EventService;
import ru.practicum.explorewithme.service.service.UserService;
import ru.practicum.explorewithme.service.specs.UserSpecifications;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final EventService eventService;
    private final UserMapper userMapper;

    @Override
    public EventFullDto saveNewEvent(Long userId, NewEventDto request) {
        User user = fetchUser(userId);
        return eventService.save(user, request);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        User user = fetchUser(userId);
        return eventService.getUsersEvents(user, from, size);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        User user = fetchUser(userId);
        return eventService.getEvent(user, eventId);
    }

    @Override
    public EventFullDto editEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        User user = fetchUser(userId);
        return eventService.edit(user, eventId, request);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        User user = fetchUser(userId);
        return eventService.getRequests(user, eventId);
    }

    @Override
    public ParticipationRequestDto saveParticipationRequest(Long userId, Long eventId) {
        User user = fetchUser(userId);
        return eventService.saveParticipationRequest(user, eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateEventParticipationStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        User user = fetchUser(userId);
        return eventService.updateParticipationStatus(user, eventId, request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        User user = fetchUser(userId);
        return eventService.getUserRequests(user);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = fetchUser(userId);
        return eventService.cancelRequest(user, requestId);
    }

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

    private User fetchUser(Long userId) {
        return userRepository.safeFetch(userId);
    }
}

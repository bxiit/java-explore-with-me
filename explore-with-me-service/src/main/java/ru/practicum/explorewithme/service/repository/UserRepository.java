package ru.practicum.explorewithme.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends EwmRepository<User> {
    @Override
    default Class<User> entityClass() {
        return User.class;
    }

    List<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}

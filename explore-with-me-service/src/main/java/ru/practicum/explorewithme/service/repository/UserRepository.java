package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.User;

@Repository
public interface UserRepository extends EwmRepository<User>, JpaSpecificationExecutor<User> {
    @Override
    default Class<User> entityClass() {
        return User.class;
    }

    int deleteUserById(Long id);
}

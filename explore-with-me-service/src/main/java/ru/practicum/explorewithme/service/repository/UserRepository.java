package ru.practicum.explorewithme.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

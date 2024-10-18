package ru.practicum.explorewithme.service.dto.user;

import lombok.Data;

@Data
public class NewUserRequest {
    private String email;
    private String name;
}

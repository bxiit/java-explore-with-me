package ru.practicum.explorewithme.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {
    @Email
    @Size(min = 6, max = 254)
    private String email;

    @Size(min = 2, max = 254)
    private String name;
}

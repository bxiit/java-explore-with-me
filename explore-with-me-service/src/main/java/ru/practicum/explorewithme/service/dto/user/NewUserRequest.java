package ru.practicum.explorewithme.service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.explorewithme.service.validation.NotOnlySpace;

@Data
public class NewUserRequest {
    @Email
    @NotNull
    @NotOnlySpace
    @Size(min = 6, max = 254)
    private String email;

    @NotNull
    @NotOnlySpace
    @Size(min = 2, max = 250)
    private String name;
}

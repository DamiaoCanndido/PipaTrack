package br.com.prestcontas.pipatrack.dto.user;

import java.util.UUID;

import br.com.prestcontas.pipatrack.validators.PasswordMatches;
import br.com.prestcontas.pipatrack.dto.PasswordConfirmable;
import br.com.prestcontas.pipatrack.entities.Role.RoleEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record UserUpdateDTO(
    @Min(value = 3, message = "Username must be at least 3 characters long")
    String username, 

    @Email(message = "Email should be valid")
    String email,

    @Enumerated(EnumType.STRING)
    RoleEnum role,

    @Size(min = 6, message = "Password must have at least 6 characters")
    String password, 

    @Size(min = 6, message = "Password must have at least 6 characters")
    String confirmPassword,

    UUID townshipId
) implements PasswordConfirmable { }

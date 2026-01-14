package br.com.prestcontas.pipatrack.dto;

import java.util.UUID;

import br.com.prestcontas.pipatrack.entities.Role.RoleEnum;
import br.com.prestcontas.pipatrack.validators.PasswordMatches;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record RegisterUserDTO(
    @NotBlank(message = "Username is required")
    String username, 

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    String email,

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Role is required")
    RoleEnum name,
    
    @Size(min = 6, message = "Password must have at least 6 characters")
    String password,

    @NotBlank(message = "Confirm password is required")
    String confirmPassword,

    UUID townshipId
) implements PasswordConfirmable {}

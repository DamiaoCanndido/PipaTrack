package br.com.prestcontas.pipatrack.dto.town;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TownRequestDTO(

    @Size(min = 3, message = "Name must be at least 3 characters long")
    @NotBlank(message = "name is required")
    String name,

    @Pattern(
        regexp = "^https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$", 
        message = "The URL must be valid."
    ) 
    @NotBlank(message = "url is required")
    String imageUrl, 

    @Size(min = 2, max = 2, message = "uf must have 2 characters")
    @NotBlank(message = "uf is required")
    String uf,

    @CNPJ(message = "invalid format")
    @NotBlank(message = "cnpj is required")
    String cnpj,

    @Min(value = 1, message = "code invalid")
    @NotBlank(message = "code is required")
    Long code
) {}

package br.com.prestcontas.pipatrack.dto.town;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TownUpdateDTO(
    
    @Size(min = 3, message = "Name must be at least 3 characters long")
    String name, 

    @Size(min = 2, max = 2, message = "uf must have 2 characters")
    String uf,

    @Pattern(
        regexp = "^https?://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$", 
        message = "The URL must be valid."
    )
    String imageUrl,

    @CNPJ(message = "invalid format")
    String cnpj,

    @Min(value = 1, message = "code invalid")
    Long code
) { }

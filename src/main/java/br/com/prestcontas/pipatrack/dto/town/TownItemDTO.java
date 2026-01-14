package br.com.prestcontas.pipatrack.dto.town;

import java.util.UUID;

public record TownItemDTO(
    UUID townId, 
    String name, 
    String uf, 
    String imageUrl,
    String cnpj,
    Long code
) { }

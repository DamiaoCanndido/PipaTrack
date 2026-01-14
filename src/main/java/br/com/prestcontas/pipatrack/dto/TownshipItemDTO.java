package br.com.prestcontas.pipatrack.dto;

import java.util.UUID;

public record TownshipItemDTO(
    UUID townshipId, 
    String name, 
    String uf, 
    String imageUrl
) { }

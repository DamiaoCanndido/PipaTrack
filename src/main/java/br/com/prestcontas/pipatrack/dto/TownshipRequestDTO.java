package br.com.prestcontas.pipatrack.dto;

public record TownshipRequestDTO(
    String name, 
    String imageUrl, 
    String uf
) {}

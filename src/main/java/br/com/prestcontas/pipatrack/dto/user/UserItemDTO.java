package br.com.prestcontas.pipatrack.dto.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.prestcontas.pipatrack.dto.role.RoleItemDTO;
import br.com.prestcontas.pipatrack.dto.town.TownItemDTO;

public record UserItemDTO(
    UUID userId, 
    String username, 
    String email,
    List<RoleItemDTO> roles, 
    TownItemDTO township,
    LocalDateTime createdAt
) { }

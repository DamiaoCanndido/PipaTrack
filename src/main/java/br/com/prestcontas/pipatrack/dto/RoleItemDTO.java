package br.com.prestcontas.pipatrack.dto;

import br.com.prestcontas.pipatrack.entities.Role;

public record RoleItemDTO(Long roleId, Role.Values name) {

}

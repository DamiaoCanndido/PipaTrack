package br.com.prestcontas.pipatrack.dto.user;

import java.util.List;

public record UserResponseDTO(List<UserItemDTO> users, 
                      int page, 
                      int pageSize, 
                      int totalPages, 
                      long totalElements) {}

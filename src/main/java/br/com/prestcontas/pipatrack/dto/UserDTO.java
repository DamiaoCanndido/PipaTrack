package br.com.prestcontas.pipatrack.dto;

import java.util.List;

public record UserDTO(List<UserItemDTO> users, 
                      int page, 
                      int pageSize, 
                      int totalPages, 
                      long totalElements) {}

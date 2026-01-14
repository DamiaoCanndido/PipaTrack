package br.com.prestcontas.pipatrack.dto.town;

import java.util.List;

public record TownResponseDTO(List<TownItemDTO> towns, 
                          int page, 
                          int pageSize, 
                          int totalPages, 
                          long totalElements) {

}

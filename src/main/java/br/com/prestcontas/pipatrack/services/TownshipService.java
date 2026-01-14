package br.com.prestcontas.pipatrack.services;

import org.springframework.data.domain.Sort;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.prestcontas.pipatrack.dto.town.TownResponseDTO;
import br.com.prestcontas.pipatrack.dto.town.TownItemDTO;
import br.com.prestcontas.pipatrack.dto.town.TownRequestDTO;
import br.com.prestcontas.pipatrack.dto.town.TownUpdateDTO;
import br.com.prestcontas.pipatrack.entities.Town;
import br.com.prestcontas.pipatrack.exception.NotFoundException;
import br.com.prestcontas.pipatrack.repositories.TownshipRepository;

@Service
public class TownshipService {

    private final TownshipRepository townshipRepo;
    
    public TownshipService(TownshipRepository townshipRepo) {
        this.townshipRepo = townshipRepo;
    }

    @Transactional(readOnly = true)
    public TownResponseDTO getAllTownships(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int pageSize) {
        var pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        var townshipPage = townshipRepo.findAll(pageable);

        var townshipItems = townshipPage.getContent().stream()
            .map(township -> new TownItemDTO(
                township.getTownId(),
                township.getName(),
                township.getUf(),
                township.getImageUrl(),
                township.getCnpj(),
                township.getCode()
            ))
            .toList();
        
        return new TownResponseDTO(
            townshipItems,
            townshipPage.getNumber(),
            townshipPage.getSize(),
            townshipPage.getTotalPages(),
            townshipPage.getTotalElements()
        );
    }

    @Transactional
    public void createTownship(TownRequestDTO dto){
        var township = new Town();
        township.setName(dto.name());
        township.setUf(dto.uf());
        township.setImageUrl(dto.imageUrl());
        townshipRepo.save(township);
    }

    protected void applyUpdates(TownUpdateDTO dto, Town town) {
        if (dto.name() != null) {
            town.setName(dto.name());
        }
        if (dto.uf() != null) {
            town.setUf(dto.uf());
        }
        if (dto.imageUrl() != null) {
            town.setImageUrl(dto.imageUrl());
        }
    }

    @Transactional
    public void updateTownship(UUID townshipId, TownUpdateDTO dto){
        var township = townshipRepo.findById(townshipId)
            .orElseThrow(() -> new NotFoundException("Township not found"));
        
        applyUpdates(dto, township);
        townshipRepo.save(township);
    }

    @Transactional
    public void deleteTownship(UUID townshipId){
        townshipRepo.findById(townshipId)
        .orElseThrow(() -> new NotFoundException(
            "Township not found"
        ));
        townshipRepo.deleteById(townshipId);
    }
}

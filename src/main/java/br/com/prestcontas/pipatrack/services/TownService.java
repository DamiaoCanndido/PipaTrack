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
import br.com.prestcontas.pipatrack.exception.ConflictException;
import br.com.prestcontas.pipatrack.exception.NotFoundException;
import br.com.prestcontas.pipatrack.repositories.TownRepository;

@Service
public class TownService {

    private final TownRepository townRepo;
    
    public TownService(TownRepository townRepo) {
        this.townRepo = townRepo;
    }

    @Transactional(readOnly = true)
    public TownResponseDTO getAllTowns(
        @RequestParam(defaultValue = "0") 
        int page,
        @RequestParam(defaultValue = "10") 
        int pageSize
    ) {
        var pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "name");
        var townPage = townRepo.findAll(pageable);

        var townItems = townPage.getContent().stream()
            .map(town -> new TownItemDTO(
                town.getTownId(),
                town.getName(),
                town.getUf(),
                town.getImageUrl(),
                town.getCnpj(),
                town.getCode()
            ))
            .toList();
        
        return new TownResponseDTO(
            townItems,
            townPage.getNumber(),
            townPage.getSize(),
            townPage.getTotalPages(),
            townPage.getTotalElements()
        );
    }

    @Transactional
    public void createTown(TownRequestDTO dto){
        var townAlreadyExists = townRepo.findByCode(dto.code());

        if(townAlreadyExists.isPresent()) {
            throw new ConflictException("There is already a town with that code.");
        }

        var town = new Town();
        town.setName(dto.name());
        town.setUf(dto.uf());
        town.setImageUrl(dto.imageUrl());
        town.setCnpj(dto.cnpj());
        town.setCode(dto.code());
        
        townRepo.save(town);
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
        if (dto.cnpj() != null) {
            town.setCnpj(dto.cnpj());
        }
        if (dto.code() != null) {
            town.setCode(dto.code());
        }
    }

    @Transactional
    public void updateTown(UUID townId, TownUpdateDTO dto){
        var town = townRepo.findById(townId)
            .orElseThrow(() -> new NotFoundException("Town not found"));
        
        applyUpdates(dto, town);
        townRepo.save(town);
    }

    @Transactional
    public void deleteTown(UUID townId){
        townRepo.findById(townId)
        .orElseThrow(() -> new NotFoundException(
            "Town not found"
        ));
        townRepo.deleteById(townId);
    }
}

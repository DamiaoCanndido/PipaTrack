package br.com.prestcontas.pipatrack.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.prestcontas.pipatrack.dto.town.TownResponseDTO;
import br.com.prestcontas.pipatrack.dto.town.TownRequestDTO;
import br.com.prestcontas.pipatrack.dto.town.TownUpdateDTO;
import br.com.prestcontas.pipatrack.services.TownService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/town")
public class TownController {

    private final TownService townService;

    public TownController(TownService townService) {
        this.townService = townService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<TownResponseDTO> getTowns(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(townService.getAllTowns(page, pageSize));
    }
    

    @PostMapping("")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> createTown(@Valid @RequestBody TownRequestDTO dto) {
        townService.createTown(dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> updateTown(@PathVariable UUID id, @Valid @RequestBody TownUpdateDTO dto) {
        townService.updateTown(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> deleteTown(@PathVariable UUID id) {
        townService.deleteTown(id);
        return ResponseEntity.ok().build();
    }
}

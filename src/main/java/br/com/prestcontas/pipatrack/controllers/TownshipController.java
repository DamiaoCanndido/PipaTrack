package br.com.prestcontas.pipatrack.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.prestcontas.pipatrack.dto.TownshipDTO;
import br.com.prestcontas.pipatrack.dto.TownshipRequestDTO;
import br.com.prestcontas.pipatrack.dto.UpdateTownshipDTO;
import br.com.prestcontas.pipatrack.services.TownshipService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/town")
public class TownshipController {

    private final TownshipService townshipService;

    public TownshipController(TownshipService townshipService) {
        this.townshipService = townshipService;
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<TownshipDTO> getTownships(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(townshipService.getAllTownships(page, pageSize));
    }
    

    @PostMapping("")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> createTown(@Valid @RequestBody TownshipRequestDTO dto) {
        townshipService.createTownship(dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> updateTown(@PathVariable UUID id, @Valid @RequestBody UpdateTownshipDTO dto) {
        townshipService.updateTownship(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> deleteTown(@PathVariable UUID id) {
        townshipService.deleteTownship(id);
        return ResponseEntity.ok().build();
    }
}

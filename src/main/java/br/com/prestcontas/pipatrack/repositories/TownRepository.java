package br.com.prestcontas.pipatrack.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.prestcontas.pipatrack.entities.Town;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TownRepository extends JpaRepository<Town, UUID>{

    Optional<Town> findByName(String name);

    Optional<Town> findByTownId(UUID townId);

    Optional<Town> findByCode(Long code);

}


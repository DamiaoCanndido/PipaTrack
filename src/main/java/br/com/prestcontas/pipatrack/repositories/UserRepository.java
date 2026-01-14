package br.com.prestcontas.pipatrack.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.prestcontas.pipatrack.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}

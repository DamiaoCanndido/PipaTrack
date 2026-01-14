package br.com.prestcontas.pipatrack.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.prestcontas.pipatrack.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByName(Role.Values name);

}

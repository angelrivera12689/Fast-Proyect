package com.app.ventas_api.seguridad.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.seguridad.domain.Role;

import java.util.Optional;

/**
 * SEGURIDAD - IRepository
 * Interface: IRoleRepository
 */
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    
    boolean existsByName(String name);
}

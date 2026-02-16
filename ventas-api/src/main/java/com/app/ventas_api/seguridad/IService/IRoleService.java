package com.app.ventas_api.seguridad.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.seguridad.domain.Role;

/**
 * SEGURIDAD - IService
 * Interface: IRoleService
 */
public interface IRoleService {
    
    List<Role> all() throws Exception;
    
    Optional<Role> findById(Long id) throws Exception;
    
    Role save(Role entity) throws Exception;
    
    void update(Long id, Role entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<Role> findByName(String name) throws Exception;
    
    boolean existsByName(String name);
}

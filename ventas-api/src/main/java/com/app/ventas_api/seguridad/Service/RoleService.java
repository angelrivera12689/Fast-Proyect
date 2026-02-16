package com.app.ventas_api.seguridad.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.seguridad.domain.Role;
import com.app.ventas_api.seguridad.IRepository.IRoleRepository;
import com.app.ventas_api.seguridad.IService.IRoleService;

/**
 * SEGURIDAD - Service
 * Implementation: RoleService
 */
@Service
public class RoleService implements IRoleService {
    
    @Autowired
    private IRoleRepository repository;
    
    @Override
    public List<Role> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<Role> findById(Long id) throws Exception {
        Optional<Role> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Role not found");
        }
        return op;
    }
    
    @Override
    public Role save(Role entity) throws Exception {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving role: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Role entity) throws Exception {
        Optional<Role> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Role not found");
        }
        
        Role entityUpdate = op.get();
        entityUpdate.setName(entity.getName());
        entityUpdate.setDescription(entity.getDescription());
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<Role> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Role not found");
        }
        
        repository.delete(op.get());
    }
    
    @Override
    public Optional<Role> findByName(String name) throws Exception {
        return repository.findByName(name);
    }
    
    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}

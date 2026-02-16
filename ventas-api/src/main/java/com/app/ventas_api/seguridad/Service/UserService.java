package com.app.ventas_api.seguridad.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.IService.IUserService;

/**
 * SEGURIDAD - Service
 * Implementation: UserService
 */
@Service
public class UserService implements IUserService {
    
    @Autowired
    private IUserRepository repository;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Override
    public List<User> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public List<User> findByStateTrue() throws Exception {
        return repository.findByActive(true);
    }
    
    @Override
    public Optional<User> findById(Long id) throws Exception {
        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("User not found");
        }
        return op;
    }
    
    @Override
    public User save(User entity) throws Exception {
        try {
            // Fetch Company entity if set
            if (entity.getCompany() != null && entity.getCompany().getId() != null) {
                entity.setCompany(companyRepository.findById(entity.getCompany().getId())
                        .orElse(null));
            }
            if (entity.getActive() == null) {
                entity.setActive(true);
            }
            if (entity.getTwoFactorEnabled() == null) {
                entity.setTwoFactorEnabled(false);
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving user: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, User entity) throws Exception {
        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("User not found");
        }
        
        User entityUpdate = op.get();
        entityUpdate.setCompany(entity.getCompany());
        entityUpdate.setUsername(entity.getUsername());
        entityUpdate.setEmail(entity.getEmail());
        entityUpdate.setPhone(entity.getPhone());
        entityUpdate.setAvatarUrl(entity.getAvatarUrl());
        entityUpdate.setTwoFactorEnabled(entity.getTwoFactorEnabled());
        entityUpdate.setTwoFactorSecret(entity.getTwoFactorSecret());
        entityUpdate.setPasswordResetToken(entity.getPasswordResetToken());
        entityUpdate.setPasswordResetExpires(entity.getPasswordResetExpires());
        if (entity.getActive() != null) {
            entityUpdate.setActive(entity.getActive());
        }
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("User not found");
        }
        
        User entityUpdate = op.get();
        entityUpdate.setActive(false);
        repository.save(entityUpdate);
    }
    
    @Override
    public Optional<User> findByUsername(String username) throws Exception {
        return repository.findByUsername(username);
    }
    
    @Override
    public Optional<User> findByEmail(String email) throws Exception {
        return repository.findByEmail(email);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
    
    @Override
    public List<User> findByActive(Boolean active) {
        return repository.findByActive(active);
    }
    
    @Override
    public List<User> findByCompanyId(Long companyId) {
        return repository.findByCompany_Id(companyId);
    }
    
    @Override
    public Optional<User> findByPasswordResetToken(String token) throws Exception {
        return repository.findByPasswordResetToken(token);
    }
}

package com.app.ventas_api.seguridad.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.seguridad.domain.RefreshToken;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IRepository.IRefreshTokenRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.IService.IRefreshTokenService;

/**
 * SEGURIDAD - Service
 * Implementation: RefreshTokenService
 */
@Service
public class RefreshTokenService implements IRefreshTokenService {
    
    @Autowired
    private IRefreshTokenRepository repository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Override
    public List<RefreshToken> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<RefreshToken> findById(Long id) throws Exception {
        Optional<RefreshToken> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("RefreshToken not found");
        }
        return op;
    }
    
    @Override
    public RefreshToken save(RefreshToken entity) throws Exception {
        try {
            // Fetch User entity if set
            if (entity.getUser() != null && entity.getUser().getId() != null) {
                entity.setUser(userRepository.findById(entity.getUser().getId())
                        .orElseThrow(() -> new Exception("User not found")));
            }
            if (entity.getRevoked() == null) {
                entity.setRevoked(false);
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving refresh token: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, RefreshToken entity) throws Exception {
        Optional<RefreshToken> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("RefreshToken not found");
        }
        
        RefreshToken entityUpdate = op.get();
        entityUpdate.setUser(entity.getUser());
        entityUpdate.setToken(entity.getToken());
        entityUpdate.setExpiresAt(entity.getExpiresAt());
        entityUpdate.setRevoked(entity.getRevoked());
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<RefreshToken> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("RefreshToken not found");
        }
        
        repository.delete(op.get());
    }
    
    @Override
    public Optional<RefreshToken> findByToken(String token) throws Exception {
        return repository.findByToken(token);
    }
    
    @Override
    public boolean existsByToken(String token) {
        return repository.existsByToken(token);
    }
    
    @Override
    public List<RefreshToken> findByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }
    
    @Override
    public List<RefreshToken> findByRevoked(Boolean revoked) {
        return repository.findByRevoked(revoked);
    }
    
    @Override
    public void revokeToken(Long id) throws Exception {
        Optional<RefreshToken> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("RefreshToken not found");
        }
        
        RefreshToken entityUpdate = op.get();
        entityUpdate.setRevoked(true);
        repository.save(entityUpdate);
    }
}

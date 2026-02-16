package com.app.ventas_api.seguridad.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.seguridad.domain.RefreshToken;

/**
 * SEGURIDAD - IService
 * Interface: IRefreshTokenService
 */
public interface IRefreshTokenService {
    
    List<RefreshToken> all() throws Exception;
    
    Optional<RefreshToken> findById(Long id) throws Exception;
    
    RefreshToken save(RefreshToken entity) throws Exception;
    
    void update(Long id, RefreshToken entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<RefreshToken> findByToken(String token) throws Exception;
    
    boolean existsByToken(String token);
    
    List<RefreshToken> findByUserId(Long userId);
    
    List<RefreshToken> findByRevoked(Boolean revoked);
    
    void revokeToken(Long id) throws Exception;
}

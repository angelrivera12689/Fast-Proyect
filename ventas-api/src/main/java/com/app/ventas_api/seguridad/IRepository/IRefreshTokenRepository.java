package com.app.ventas_api.seguridad.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.seguridad.domain.RefreshToken;

import java.util.Optional;
import java.util.List;

/**
 * SEGURIDAD - IRepository
 * Interface: IRefreshTokenRepository
 */
@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByToken(String token);
    
    boolean existsByToken(String token);
    
    List<RefreshToken> findByUser_Id(Long userId);
    
    List<RefreshToken> findByRevoked(Boolean revoked);
}

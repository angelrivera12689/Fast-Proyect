package com.app.ventas_api.seguridad.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.seguridad.domain.User;

import java.util.Optional;
import java.util.List;

/**
 * SEGURIDAD - IRepository
 * Interface: IUserRepository
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByActive(Boolean active);
    
    List<User> findByCompany_Id(Long companyId);
    
    Optional<User> findByPasswordResetToken(String token);
}

package com.app.ventas_api.seguridad.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.seguridad.domain.User;

/**
 * SEGURIDAD - IService
 * Interface: IUserService
 */
public interface IUserService {
    
    List<User> all() throws Exception;
    
    List<User> findByStateTrue() throws Exception;
    
    Optional<User> findById(Long id) throws Exception;
    
    User save(User entity) throws Exception;
    
    void update(Long id, User entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<User> findByUsername(String username) throws Exception;
    
    Optional<User> findByEmail(String email) throws Exception;
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByActive(Boolean active);
    
    List<User> findByCompanyId(Long companyId);
    
    Optional<User> findByPasswordResetToken(String token) throws Exception;
}

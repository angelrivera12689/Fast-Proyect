package com.app.ventas_api.Organizacion.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.Organizacion.Entity.Company;

/**
 * ORGANIZACION - IService
 * Interface: ICompanyService
 */
public interface ICompanyService {
    
    List<Company> all() throws Exception;
    
    List<Company> findByStateTrue() throws Exception;
    
    Optional<Company> findById(Long id) throws Exception;
    
    Company save(Company entity) throws Exception;
    
    void update(Long id, Company entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<Company> findByNit(String nit) throws Exception;
    
    Optional<Company> findByEmail(String email) throws Exception;
    
    boolean existsByNit(String nit);
    
    boolean existsByEmail(String email);
    
    List<Company> findByActive(Boolean active);
}

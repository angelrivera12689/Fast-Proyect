package com.app.ventas_api.Organizacion.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.Organizacion.Entity.Company;

import java.util.Optional;
import java.util.List;

/**
 * ORGANIZACION - IRepository
 * Interface: ICompanyRepository
 */
@Repository
public interface ICompanyRepository extends JpaRepository<Company, Long> {
    
    Optional<Company> findByNit(String nit);
    
    Optional<Company> findByEmail(String email);
    
    boolean existsByNit(String nit);
    
    boolean existsByEmail(String email);
    
    List<Company> findByActive(Boolean active);
}

package com.app.ventas_api.Organizacion.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.Organizacion.IService.ICompanyService;

/**
 * ORGANIZACION - Service
 * Implementation: CompanyService
 */
@Service
public class CompanyService implements ICompanyService {
    
    @Autowired
    private ICompanyRepository repository;
    
    @Override
    public List<Company> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public List<Company> findByStateTrue() throws Exception {
        return repository.findByActive(true);
    }
    
    @Override
    public Optional<Company> findById(Long id) throws Exception {
        Optional<Company> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Company not found");
        }
        return op;
    }
    
    @Override
    public Company save(Company entity) throws Exception {
        try {
            if (entity.getActive() == null) {
                entity.setActive(true);
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving company: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Company entity) throws Exception {
        Optional<Company> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Company not found");
        }
        
        Company entityUpdate = op.get();
        entityUpdate.setNit(entity.getNit());
        entityUpdate.setBusinessName(entity.getBusinessName());
        entityUpdate.setEmail(entity.getEmail());
        entityUpdate.setPhone(entity.getPhone());
        entityUpdate.setAddress(entity.getAddress());
        entityUpdate.setLogoUrl(entity.getLogoUrl());
        if (entity.getActive() != null) {
            entityUpdate.setActive(entity.getActive());
        }
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<Company> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Company not found");
        }
        
        Company entityUpdate = op.get();
        entityUpdate.setActive(false);
        repository.save(entityUpdate);
    }
    
    @Override
    public Optional<Company> findByNit(String nit) throws Exception {
        return repository.findByNit(nit);
    }
    
    @Override
    public Optional<Company> findByEmail(String email) throws Exception {
        return repository.findByEmail(email);
    }
    
    @Override
    public boolean existsByNit(String nit) {
        return repository.existsByNit(nit);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
    
    @Override
    public List<Company> findByActive(Boolean active) {
        return repository.findByActive(active);
    }
}

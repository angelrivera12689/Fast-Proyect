package com.app.ventas_api.Productos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IService.ICategoryService;

/**
 * PRODUCTOS - Service
 * Implementation: CategoryService
 */
@Service
public class CategoryService implements ICategoryService {
    
    @Autowired
    private ICategoryRepository repository;
    
    @Override
    public List<Category> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public List<Category> findByStateTrue() throws Exception {
        return repository.findByActive(true);
    }
    
    @Override
    public Optional<Category> findById(Long id) throws Exception {
        Optional<Category> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Category not found");
        }
        return op;
    }
    
    @Override
    public Category save(Category entity) throws Exception {
        try {
            // Fetch Parent category entity if set
            if (entity.getParent() != null && entity.getParent().getId() != null) {
                entity.setParent(repository.findById(entity.getParent().getId())
                        .orElse(null));
            }
            if (entity.getActive() == null) {
                entity.setActive(true);
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving category: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Category entity) throws Exception {
        Optional<Category> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Category not found");
        }
        
        Category entityUpdate = op.get();
        entityUpdate.setName(entity.getName());
        entityUpdate.setDescription(entity.getDescription());
        entityUpdate.setParent(entity.getParent());
        entityUpdate.setImageUrl(entity.getImageUrl());
        if (entity.getActive() != null) {
            entityUpdate.setActive(entity.getActive());
        }
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<Category> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Category not found");
        }
        
        Category entityUpdate = op.get();
        entityUpdate.setActive(false);
        repository.save(entityUpdate);
    }
    
    @Override
    public Optional<Category> findByName(String name) throws Exception {
        return repository.findByName(name);
    }
    
    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
    
    @Override
    public List<Category> findByActive(Boolean active) {
        return repository.findByActive(active);
    }
    
    @Override
    public List<Category> findByParentId(Long parentId) {
        return repository.findByParent_Id(parentId);
    }
}

package com.app.ventas_api.Productos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IService.ICategoryService;
import com.app.ventas_api.config.exception.ResourceNotFoundException;

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
            throw new ResourceNotFoundException("Category", "id", id);
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
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error saving category: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Category entity) throws Exception {
        Optional<Category> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ResourceNotFoundException("Category", "id", id);
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
            throw new ResourceNotFoundException("Category", "id", id);
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
    
    // Métodos con lógica de negocio encapsulada
    @Override
    public Category create(String name, String description, Long parentId, String imageUrl, Boolean active) throws Exception {
        Category category = Category.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .active(active != null ? active : true)
                .build();
        
        // Buscar y asignar categoría padre real desde BD
        if (parentId != null) {
            Category parent = repository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", parentId));
            category.setParent(parent);
        }
        
        return repository.save(category);
    }
    
    @Override
    public void updateCategory(Long id, String name, String description, Long parentId, String imageUrl, Boolean active) throws Exception {
        Category existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        existing.setName(name);
        existing.setDescription(description);
        existing.setImageUrl(imageUrl);
        if (active != null) {
            existing.setActive(active);
        }
        
        // Buscar y asignar categoría padre real desde BD
        if (parentId != null) {
            Category parent = repository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", parentId));
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }
        
        repository.save(existing);
    }
}

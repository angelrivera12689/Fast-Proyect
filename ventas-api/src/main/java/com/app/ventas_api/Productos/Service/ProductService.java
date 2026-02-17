package com.app.ventas_api.Productos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Productos.IService.IProductService;

/**
 * PRODUCTOS - Service
 * Implementation: ProductService
 */
@Service
public class ProductService implements IProductService {
    
    @Autowired
    private IProductRepository repository;
    
    @Autowired
    private ICategoryRepository categoryRepository;
    
    @Override
    public List<Product> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Page<Product> findAll(Pageable pageable) throws Exception {
        return repository.findAll(pageable);
    }
    
    @Override
    public List<Product> findByStateTrue() throws Exception {
        return repository.findByActive(true);
    }
    
    @Override
    public Optional<Product> findById(Long id) throws Exception {
        Optional<Product> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Product not found");
        }
        return op;
    }
    
    @Override
    public Product save(Product entity) throws Exception {
        try {
            // Fetch and set Category entity
            if (entity.getCategory() != null && entity.getCategory().getId() != null) {
                entity.setCategory(categoryRepository.findById(entity.getCategory().getId())
                        .orElseThrow(() -> new Exception("Category not found")));
            }
            if (entity.getActive() == null) {
                entity.setActive(true);
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving product: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Product entity) throws Exception {
        Optional<Product> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Product not found");
        }
        
        Product entityUpdate = op.get();
        entityUpdate.setCategory(entity.getCategory());
        entityUpdate.setName(entity.getName());
        entityUpdate.setDescription(entity.getDescription());
        entityUpdate.setBasePrice(entity.getBasePrice());
        entityUpdate.setPriceTiers(entity.getPriceTiers());
        entityUpdate.setStock(entity.getStock());
        entityUpdate.setSku(entity.getSku());
        entityUpdate.setWeight(entity.getWeight());
        entityUpdate.setDimensions(entity.getDimensions());
        entityUpdate.setImageUrl(entity.getImageUrl());
        if (entity.getActive() != null) {
            entityUpdate.setActive(entity.getActive());
        }
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<Product> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Product not found");
        }
        
        Product entityUpdate = op.get();
        entityUpdate.setActive(false);
        repository.save(entityUpdate);
    }
    
    @Override
    public Optional<Product> findBySku(String sku) throws Exception {
        return repository.findBySku(sku);
    }
    
    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }
    
    @Override
    public List<Product> findByActive(Boolean active) {
        return repository.findByActive(active);
    }
    
    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return repository.findByCategory_Id(categoryId);
    }
    
    @Override
    public List<Product> findByNameContaining(String name) {
        return repository.findByNameContaining(name);
    }
}

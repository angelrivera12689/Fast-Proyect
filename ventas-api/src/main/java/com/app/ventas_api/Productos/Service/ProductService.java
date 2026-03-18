package com.app.ventas_api.Productos.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Laboratory;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IRepository.ILaboratoryRepository;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Productos.IService.IProductService;
import com.app.ventas_api.config.exception.ResourceNotFoundException;

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
    
    @Autowired
    private ILaboratoryRepository laboratoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) throws Exception {
        return repository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByStateTrue() throws Exception {
        return repository.findByActive(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) throws Exception {
        Optional<Product> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        return op;
    }
    
    @Override
    @Transactional
    public Product save(Product entity) throws Exception {
        try {
            // Fetch and set Category entity
            if (entity.getCategory() != null && entity.getCategory().getId() != null) {
                entity.setCategory(categoryRepository.findById(entity.getCategory().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", entity.getCategory().getId())));
            }
            // Fetch and set Laboratory entity
            if (entity.getLaboratory() != null && entity.getLaboratory().getId() != null) {
                entity.setLaboratory(laboratoryRepository.findById(entity.getLaboratory().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Laboratory", "id", entity.getLaboratory().getId())));
            }
            if (entity.getActive() == null) {
                entity.setActive(true);
            }
            return repository.save(entity);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Error saving product: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void update(Long id, Product entity) throws Exception {
        Optional<Product> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ResourceNotFoundException("Product", "id", id);
        }

        Product entityUpdate = op.get();

        // Actualizar campos básicos
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

        // Atributos de medicamentos - asegurarse de que se actualicen siempre
        entityUpdate.setLaboratory(entity.getLaboratory());
        entityUpdate.setRegistrationNumber(entity.getRegistrationNumber());
        entityUpdate.setDosage(entity.getDosage());
        entityUpdate.setExpirationDate(entity.getExpirationDate());
        entityUpdate.setActiveIngredient(entity.getActiveIngredient());
        entityUpdate.setPresentation(entity.getPresentation());
        entityUpdate.setRequiresPrescription(entity.getRequiresPrescription());

        // Estado activo
        if (entity.getActive() != null) {
            entityUpdate.setActive(entity.getActive());
        }

        repository.save(entityUpdate);
    }
    
    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        Optional<Product> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ResourceNotFoundException("Product", "id", id);
        }
        
        Product entityUpdate = op.get();
        entityUpdate.setActive(false);
        repository.save(entityUpdate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findBySku(String sku) throws Exception {
        return repository.findBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByActive(Boolean active) {
        return repository.findByActive(active);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(Long categoryId) {
        return repository.findByCategory_Id(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByNameContaining(String name) {
        return repository.findByNameContaining(name);
    }
}

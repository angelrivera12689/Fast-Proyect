package com.app.ventas_api.Productos.Service;

import com.app.ventas_api.Productos.Entity.ProductImage;
import com.app.ventas_api.Productos.IRepository.IProductImageRepository;
import com.app.ventas_api.Productos.IService.IProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * PRODUCTOS - Service
 * Implementation: ProductImageService
 */
@Service
public class ProductImageService implements IProductImageService {
    
    @Autowired
    private IProductImageRepository repository;
    
    @Override
    public List<ProductImage> findAll() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<ProductImage> findById(Long id) throws Exception {
        Optional<ProductImage> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("ProductImage not found");
        }
        return op;
    }
    
    @Override
    public List<ProductImage> findByProductId(Long productId) throws Exception {
        return repository.findByProductIdOrderBySortOrderAsc(productId);
    }
    
    @Override
    public ProductImage save(ProductImage entity) throws Exception {
        return repository.save(entity);
    }
    
    @Override
    public void update(Long id, ProductImage entity) throws Exception {
        Optional<ProductImage> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("ProductImage not found");
        }
        
        ProductImage imageUpdate = op.get();
        imageUpdate.setUrl(entity.getUrl());
        imageUpdate.setIsPrimary(entity.getIsPrimary());
        imageUpdate.setSortOrder(entity.getSortOrder());
        imageUpdate.setAltText(entity.getAltText());
        
        repository.save(imageUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<ProductImage> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("ProductImage not found");
        }
        repository.delete(op.get());
    }
    
    @Override
    public void deleteByProductId(Long productId) throws Exception {
        repository.deleteByProductId(productId);
    }
}

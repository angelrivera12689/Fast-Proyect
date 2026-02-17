package com.app.ventas_api.Productos.IService;

import com.app.ventas_api.Productos.Entity.ProductImage;
import java.util.List;
import java.util.Optional;

/**
 * PRODUCTOS - IService
 * Interface: IProductImageService
 */
public interface IProductImageService {
    
    List<ProductImage> findAll() throws Exception;
    
    Optional<ProductImage> findById(Long id) throws Exception;
    
    List<ProductImage> findByProductId(Long productId) throws Exception;
    
    ProductImage save(ProductImage entity) throws Exception;
    
    void update(Long id, ProductImage entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    void deleteByProductId(Long productId) throws Exception;
}

package com.app.ventas_api.Productos.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.Productos.Entity.Product;

/**
 * PRODUCTOS - IService
 * Interface: IProductService
 */
public interface IProductService {
    
    List<Product> all() throws Exception;
    
    List<Product> findByStateTrue() throws Exception;
    
    Optional<Product> findById(Long id) throws Exception;
    
    Product save(Product entity) throws Exception;
    
    void update(Long id, Product entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<Product> findBySku(String sku) throws Exception;
    
    boolean existsBySku(String sku);
    
    List<Product> findByActive(Boolean active);
    
    List<Product> findByCategoryId(Long categoryId);
    
    List<Product> findByNameContaining(String name);
}

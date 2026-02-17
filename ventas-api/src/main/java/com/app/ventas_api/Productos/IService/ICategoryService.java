package com.app.ventas_api.Productos.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.Productos.Entity.Category;

/**
 * PRODUCTOS - IService
 * Interface: ICategoryService
 */
public interface ICategoryService {
    
    List<Category> all() throws Exception;
    
    List<Category> findByStateTrue() throws Exception;
    
    Optional<Category> findById(Long id) throws Exception;
    
    Category save(Category entity) throws Exception;
    
    void update(Long id, Category entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<Category> findByName(String name) throws Exception;
    
    boolean existsByName(String name);
    
    List<Category> findByActive(Boolean active);
    
    List<Category> findByParentId(Long parentId);
    
    // Métodos con lógica de negocio encapsulada
    Category create(String name, String description, Long parentId, String imageUrl, Boolean active) throws Exception;
    
    void updateCategory(Long id, String name, String description, Long parentId, String imageUrl, Boolean active) throws Exception;
}

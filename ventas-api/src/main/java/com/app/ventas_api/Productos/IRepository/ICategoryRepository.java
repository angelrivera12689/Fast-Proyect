package com.app.ventas_api.Productos.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.Productos.Entity.Category;

import java.util.Optional;
import java.util.List;

/**
 * PRODUCTOS - IRepository
 * Interface: ICategoryRepository
 */
@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Category> findByActive(Boolean active);
    
    List<Category> findByParent_Id(Long parentId);
}

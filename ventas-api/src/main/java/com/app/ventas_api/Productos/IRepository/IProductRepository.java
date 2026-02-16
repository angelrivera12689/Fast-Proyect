package com.app.ventas_api.Productos.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.Productos.Entity.Product;

import java.util.Optional;
import java.util.List;

/**
 * PRODUCTOS - IRepository
 * Interface: IProductRepository
 */
@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
    
    boolean existsBySku(String sku);
    
    List<Product> findByActive(Boolean active);
    
    List<Product> findByCategory_Id(Long categoryId);
    
    List<Product> findByNameContaining(String name);
}

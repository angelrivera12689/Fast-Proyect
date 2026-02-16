package com.app.ventas_api.Productos.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // Métodos para gestión de stock
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id = :productId AND p.stock >= :quantity")
    int reduceStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock + :quantity WHERE p.id = :productId")
    int increaseStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}

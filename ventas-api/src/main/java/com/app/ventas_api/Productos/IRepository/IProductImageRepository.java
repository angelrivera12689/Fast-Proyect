package com.app.ventas_api.Productos.IRepository;

import com.app.ventas_api.Productos.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PRODUCTOS - IRepository
 * Interface: IProductImageRepository
 */
@Repository
public interface IProductImageRepository extends JpaRepository<ProductImage, Long> {
    
    List<ProductImage> findByProductId(Long productId);
    
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);
    
    Optional<ProductImage> findByProductIdAndIsPrimaryTrue(Long productId);
    
    void deleteByProductId(Long productId);
}

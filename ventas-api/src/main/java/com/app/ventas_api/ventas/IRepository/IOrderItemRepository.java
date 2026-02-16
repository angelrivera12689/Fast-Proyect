package com.app.ventas_api.ventas.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.ventas.domain.OrderItem;

import java.util.List;
import java.util.Optional;

/**
 * VENTAS - IRepository
 * Interface: IOrderItemRepository
 */
@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrder_Id(Long orderId);
    
    Optional<OrderItem> findByOrder_IdAndId(Long orderId, Long id);
    
    List<OrderItem> findByProduct_Id(Long productId);
}

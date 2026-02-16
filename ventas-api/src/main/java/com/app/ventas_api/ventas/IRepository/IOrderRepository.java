package com.app.ventas_api.ventas.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.ventas.domain.Order;

import java.util.Optional;
import java.util.List;

/**
 * VENTAS - IRepository
 * Interface: IOrderRepository
 */
@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCompany_Id(Long companyId);
    
    List<Order> findByUser_Id(Long userId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    Optional<Order> findByCompany_IdAndId(Long companyId, Long id);
    
    // Métodos para carrito
    Optional<Order> findByUser_IdAndStatus(Long userId, Order.OrderStatus status);
    
    Optional<Order> findByCompany_IdAndStatus(Long companyId, Order.OrderStatus status);
}

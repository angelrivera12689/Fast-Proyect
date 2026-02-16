package com.app.ventas_api.ventas.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.ventas.domain.OrderItem;

/**
 * VENTAS - IService
 * Interface: IOrderItemService
 */
public interface IOrderItemService {
    
    List<OrderItem> all() throws Exception;
    
    Optional<OrderItem> findById(Long id) throws Exception;
    
    OrderItem save(OrderItem entity) throws Exception;
    
    void update(Long id, OrderItem entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    List<OrderItem> findByOrderId(Long orderId);
    
    Optional<OrderItem> findByOrderIdAndId(Long orderId, Long id) throws Exception;
    
    List<OrderItem> findByProductId(Long productId);
}

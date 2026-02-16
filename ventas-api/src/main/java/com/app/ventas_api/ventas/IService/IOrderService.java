package com.app.ventas_api.ventas.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.ventas.domain.Order;

/**
 * VENTAS - IService
 * Interface: IOrderService
 */
public interface IOrderService {
    
    List<Order> all() throws Exception;
    
    Optional<Order> findById(Long id) throws Exception;
    
    Order save(Order entity) throws Exception;
    
    void update(Long id, Order entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    List<Order> findByCompanyId(Long companyId);
    
    List<Order> findByUserId(Long userId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    Optional<Order> findByCompanyIdAndId(Long companyId, Long id) throws Exception;
}

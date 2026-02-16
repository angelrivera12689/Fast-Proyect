package com.app.ventas_api.ventas.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.OrderItem;

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
    
    // ==================== Métodos del Carrito ====================
    
    /**
     * Obtener o crear carrito para un usuario
     */
    Order getOrCreateCart(Long userId, Long companyId) throws Exception;
    
    /**
     * Agregar producto al carrito
     */
    Order addToCart(Long userId, Long companyId, Long productId, Integer quantity) throws Exception;
    
    /**
     * Actualizar cantidad de un item en el carrito
     */
    Order updateCartItem(Long cartId, Long itemId, Integer quantity) throws Exception;
    
    /**
     * Eliminar item del carrito
     */
    Order removeFromCart(Long cartId, Long itemId) throws Exception;
    
    /**
     * Obtener carrito activo del usuario
     */
    Optional<Order> getActiveCart(Long userId);
    
    /**
     * Convertir carrito a pedido (para pago)
     */
    Order checkout(Long cartId, String shippingAddress, String notes) throws Exception;
    
    /**
     * Confirmar pago y reducir stock
     */
    Order confirmPayment(Long orderId) throws Exception;
    
    /**
     * Cancelar pedido y restaurar stock
     */
    Order cancelOrder(Long orderId) throws Exception;
}

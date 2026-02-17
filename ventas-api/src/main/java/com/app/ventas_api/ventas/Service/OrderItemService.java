package com.app.ventas_api.ventas.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.OrderItem;
import com.app.ventas_api.ventas.IRepository.IOrderItemRepository;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.ventas.IService.IOrderItemService;

/**
 * VENTAS - Service
 * Implementation: OrderItemService
 * 
 * SEGURIDAD DE TRANSACCIONES: Este servicio maneja la reducción de stock
 * de forma atómica para prevenir race conditions.
 */
@Service
public class OrderItemService implements IOrderItemService {
    
    @Autowired
    private IOrderItemRepository repository;
    
    @Autowired
    private IOrderRepository orderRepository;
    
    @Autowired
    private IProductRepository productRepository;
    
    @Override
    public List<OrderItem> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<OrderItem> findById(Long id) throws Exception {
        Optional<OrderItem> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("OrderItem not found");
        }
        return op;
    }
    
    @Override
    @Transactional
    public OrderItem save(OrderItem entity) throws Exception {
        try {
            // Fetch Order entity
            if (entity.getOrder() != null && entity.getOrder().getId() != null) {
                entity.setOrder(orderRepository.findById(entity.getOrder().getId())
                        .orElseThrow(() -> new Exception("Order not found")));
            }
            
            // ========== RACE CONDITION PROTECTION ==========
            // Usar pessimistic lock para evitar race conditions en stock
            if (entity.getProduct() != null && entity.getProduct().getId() != null) {
                Long productId = entity.getProduct().getId();
                Integer quantity = entity.getQuantity();
                
                // 1. Obtener producto con lock (bloquea la fila)
                Product product = productRepository.findByIdWithLock(productId)
                        .orElseThrow(() -> new Exception("Product not found"));
                
                // 2. Verificar stock disponible
                if (product.getStock() < quantity) {
                    throw new Exception("Stock insuficiente. Disponible: " + product.getStock() + ", Solicitado: " + quantity);
                }
                
                // 3. Reducir stock
                product.setStock(product.getStock() - quantity);
                productRepository.save(product);
                
                // 4. Guardar referencia del producto (no crear nuevo)
                entity.setProduct(product);
            }
            // =================================================
            
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving order item: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void update(Long id, OrderItem entity) throws Exception {
        Optional<OrderItem> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("OrderItem not found");
        }
        
        OrderItem entityUpdate = op.get();
        entityUpdate.setOrder(entity.getOrder());
        entityUpdate.setProduct(entity.getProduct());
        entityUpdate.setProductName(entity.getProductName());
        entityUpdate.setProductSku(entity.getProductSku());
        entityUpdate.setQuantity(entity.getQuantity());
        entityUpdate.setUnitPrice(entity.getUnitPrice());
        entityUpdate.setSubtotal(entity.getSubtotal());
        
        repository.save(entityUpdate);
    }
    
    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        Optional<OrderItem> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("OrderItem not found");
        }
        
        // Restaurar stock al eliminar item
        OrderItem item = op.get();
        if (item.getProduct() != null && item.getQuantity() != null) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        repository.delete(op.get());
    }
    
    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return repository.findByOrder_Id(orderId);
    }
    
    @Override
    public Optional<OrderItem> findByOrderIdAndId(Long orderId, Long id) throws Exception {
        return repository.findByOrder_IdAndId(orderId, id);
    }
    
    @Override
    public List<OrderItem> findByProductId(Long productId) {
        return repository.findByProduct_Id(productId);
    }
}

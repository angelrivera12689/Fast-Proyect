package com.app.ventas_api.ventas.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.OrderItem;
import com.app.ventas_api.ventas.IRepository.IOrderItemRepository;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.ventas.IService.IOrderItemService;

/**
 * VENTAS - Service
 * Implementation: OrderItemService
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
    public OrderItem save(OrderItem entity) throws Exception {
        try {
            // Fetch Order entity
            if (entity.getOrder() != null && entity.getOrder().getId() != null) {
                entity.setOrder(orderRepository.findById(entity.getOrder().getId())
                        .orElseThrow(() -> new Exception("Order not found")));
            }
            // Fetch Product entity
            if (entity.getProduct() != null && entity.getProduct().getId() != null) {
                entity.setProduct(productRepository.findById(entity.getProduct().getId())
                        .orElseThrow(() -> new Exception("Product not found")));
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving order item: " + e.getMessage());
        }
    }
    
    @Override
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
    public void delete(Long id) throws Exception {
        Optional<OrderItem> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("OrderItem not found");
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

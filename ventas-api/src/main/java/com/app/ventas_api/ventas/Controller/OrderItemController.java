package com.app.ventas_api.ventas.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ventas_api.ventas.DTO.Request.OrderItemRequestDto;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.ventas.domain.OrderItem;
import com.app.ventas_api.ventas.IService.IOrderItemService;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.Productos.IRepository.IProductRepository;

/**
 * VENTAS - Controller
 * OrderItemController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/order-items")
public class OrderItemController {
    
    @Autowired
    private IOrderItemService orderItemService;
    
    @Autowired
    private IOrderRepository orderRepository;
    
    @Autowired
    private IProductRepository productRepository;
    
    @GetMapping
    public ResponseEntity<List<OrderItem>> findAll() {
        try {
            List<OrderItem> items = orderItemService.all();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> findById(@PathVariable Long id) {
        try {
            Optional<OrderItem> item = orderItemService.findById(id);
            return item.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> findByOrder(@PathVariable Long orderId) {
        try {
            List<OrderItem> items = orderItemService.findByOrderId(orderId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItem>> findByProduct(@PathVariable Long productId) {
        try {
            List<OrderItem> items = orderItemService.findByProductId(productId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<OrderItem> create(@RequestBody OrderItemRequestDto request) {
        try {
            // Fetch Order and Product entities
            Order order = orderRepository.findById(request.getOrderId()).orElse(null);
            Product product = productRepository.findById(request.getProductId()).orElse(null);
            
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(request.getProductName())
                    .productSku(request.getProductSku())
                    .quantity(request.getQuantity())
                    .unitPrice(request.getUnitPrice())
                    .subtotal(request.getSubtotal())
                    .build();
            
            OrderItem saved = orderItemService.save(item);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> update(@PathVariable Long id, @RequestBody OrderItemRequestDto request) {
        try {
            // Fetch Order and Product entities
            Order order = orderRepository.findById(request.getOrderId()).orElse(null);
            Product product = productRepository.findById(request.getProductId()).orElse(null);
            
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(request.getProductName())
                    .productSku(request.getProductSku())
                    .quantity(request.getQuantity())
                    .unitPrice(request.getUnitPrice())
                    .subtotal(request.getSubtotal())
                    .build();
            
            orderItemService.update(id, item);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            orderItemService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

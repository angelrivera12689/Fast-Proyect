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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.ventas_api.ventas.DTO.Request.OrderRequestDto;
import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.ventas.IService.IOrderService;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;

/**
 * VENTAS - Controller
 * OrderController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/orders")
public class OrderController {
    
    @Autowired
    private IOrderService orderService;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        try {
            List<Order> orders = orderService.all();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        try {
            Optional<Order> order = orderService.findById(id);
            return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Order>> findByCompany(@PathVariable Long companyId) {
        try {
            List<Order> orders = orderService.findByCompanyId(companyId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> findByUser(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> findByStatus(@PathVariable Order.OrderStatus status) {
        try {
            List<Order> orders = orderService.findByStatus(status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderRequestDto request) {
        try {
            // Fetch Company and User entities
            Company company = companyRepository.findById(request.getCompanyId()).orElse(null);
            User user = userRepository.findById(request.getUserId()).orElse(null);
            
            Order order = Order.builder()
                    .company(company)
                    .user(user)
                    .totalAmount(request.getTotalAmount())
                    .status(request.getStatus() != null ? request.getStatus() : Order.OrderStatus.CART)
                    .shippingAddress(request.getShippingAddress())
                    .notes(request.getNotes())
                    .build();
            
            Order saved = orderService.save(order);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody OrderRequestDto request) {
        try {
            // Fetch Company and User entities
            Company company = companyRepository.findById(request.getCompanyId()).orElse(null);
            User user = userRepository.findById(request.getUserId()).orElse(null);
            
            Order order = Order.builder()
                    .company(company)
                    .user(user)
                    .totalAmount(request.getTotalAmount())
                    .status(request.getStatus())
                    .shippingAddress(request.getShippingAddress())
                    .notes(request.getNotes())
                    .build();
            
            orderService.update(id, order);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            orderService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ==================== Endpoints del Carrito ====================
    
    /**
     * Obtener o crear carrito activo del usuario
     * GET /api/orders/cart?userId=1&companyId=1
     */
    @GetMapping("/cart")
    public ResponseEntity<Order> getOrCreateCart(
            @RequestParam Long userId, 
            @RequestParam Long companyId) {
        try {
            Order cart = orderService.getOrCreateCart(userId, companyId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtener carrito activo del usuario (solo lectura)
     * GET /api/orders/cart/active/{userId}
     */
    @GetMapping("/cart/active/{userId}")
    public ResponseEntity<Order> getActiveCart(@PathVariable Long userId) {
        try {
            Optional<Order> cart = orderService.getActiveCart(userId);
            return cart.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Agregar producto al carrito
     * POST /api/orders/cart/add?userId=1&companyId=1&productId=2&quantity=3
     */
    @PostMapping("/cart/add")
    public ResponseEntity<Order> addToCart(
            @RequestParam Long userId,
            @RequestParam Long companyId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try {
            Order cart = orderService.addToCart(userId, companyId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualizar cantidad de item en el carrito
     * PUT /api/orders/cart/{cartId}/item/{itemId}?quantity=5
     */
    @PutMapping("/cart/{cartId}/item/{itemId}")
    public ResponseEntity<Order> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        try {
            Order cart = orderService.updateCartItem(cartId, itemId, quantity);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Eliminar item del carrito
     * DELETE /api/orders/cart/{cartId}/item/{itemId}
     */
    @DeleteMapping("/cart/{cartId}/item/{itemId}")
    public ResponseEntity<Order> removeFromCart(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        try {
            Order cart = orderService.removeFromCart(cartId, itemId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Checkout - Convertir carrito a pedido
     * POST /api/orders/cart/{cartId}/checkout
     */
    @PostMapping("/cart/{cartId}/checkout")
    public ResponseEntity<Order> checkout(
            @PathVariable Long cartId,
            @RequestBody CheckoutRequest request) {
        try {
            Order order = orderService.checkout(cartId, request.getShippingAddress(), request.getNotes());
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Confirmar pago (Webhook)
     * POST /api/orders/{orderId}/confirm-payment
     */
    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<Order> confirmPayment(@PathVariable Long orderId) {
        try {
            Order order = orderService.confirmPayment(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Cancelar pedido
     * POST /api/orders/{orderId}/cancel
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DTO para checkout
    public static class CheckoutRequest {
        private String shippingAddress;
        private String notes;
        
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}

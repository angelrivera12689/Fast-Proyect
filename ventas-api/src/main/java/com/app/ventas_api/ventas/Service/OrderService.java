package com.app.ventas_api.ventas.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.OrderItem;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.ventas.IService.IOrderService;

/**
 * VENTAS - Service
 * Implementation: OrderService
 */
@Service
public class OrderService implements IOrderService {
    
    @Autowired
    private IOrderRepository repository;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private IProductRepository productRepository;
    
    @Override
    public List<Order> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<Order> findById(Long id) throws Exception {
        Optional<Order> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Order not found");
        }
        return op;
    }
    
    @Override
    @Transactional
    public Order save(Order entity) throws Exception {
        try {
            // Fetch and set Company entity
            if (entity.getCompany() != null && entity.getCompany().getId() != null) {
                entity.setCompany(companyRepository.findById(entity.getCompany().getId())
                        .orElseThrow(() -> new Exception("Company not found")));
            }
            // Fetch and set User entity
            if (entity.getUser() != null && entity.getUser().getId() != null) {
                entity.setUser(userRepository.findById(entity.getUser().getId())
                        .orElseThrow(() -> new Exception("User not found")));
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving order: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void update(Long id, Order entity) throws Exception {
        Optional<Order> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Order not found");
        }
        
        Order entityUpdate = op.get();
        entityUpdate.setCompany(entity.getCompany());
        entityUpdate.setUser(entity.getUser());
        entityUpdate.setTotalAmount(entity.getTotalAmount());
        entityUpdate.setStatus(entity.getStatus());
        entityUpdate.setShippingAddress(entity.getShippingAddress());
        entityUpdate.setNotes(entity.getNotes());
        
        repository.save(entityUpdate);
    }
    
    @Override
    @Transactional
    public void delete(Long id) throws Exception {
        Optional<Order> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Order not found");
        }
        
        repository.delete(op.get());
    }
    
    @Override
    public List<Order> findByCompanyId(Long companyId) {
        return repository.findByCompany_Id(companyId);
    }
    
    @Override
    public List<Order> findByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }
    
    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        return repository.findByStatus(status);
    }
    
    @Override
    public Optional<Order> findByCompanyIdAndId(Long companyId, Long id) throws Exception {
        return repository.findByCompany_IdAndId(companyId, id);
    }
    
    // ==================== Métodos del Carrito ====================
    
    @Override
    @Transactional
    public Order getOrCreateCart(Long userId, Long companyId) throws Exception {
        // Buscar carrito activo del usuario
        Optional<Order> existingCart = repository.findByUser_IdAndStatus(userId, Order.OrderStatus.CART);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        
        // Crear nuevo carrito
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
        
        var company = companyRepository.findById(companyId)
                .orElseThrow(() -> new Exception("Company not found"));
        
        Order cart = Order.builder()
                .user(user)
                .company(company)
                .status(Order.OrderStatus.CART)
                .totalAmount(BigDecimal.ZERO)
                .items(new java.util.ArrayList<>())
                .build();
        
        return repository.save(cart);
    }
    
    @Override
    @Transactional
    public Order addToCart(Long userId, Long companyId, Long productId, Integer quantity) throws Exception {
        // Obtener o crear carrito
        Order cart = getOrCreateCart(userId, companyId);
        
        // Obtener producto
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));
        
        if (!product.getActive()) {
            throw new Exception("Product is not available");
        }
        
        // Verificar stock disponible
        if (product.getStock() < quantity) {
            throw new Exception("Insufficient stock. Available: " + product.getStock());
        }
        
        // Verificar si el producto ya está en el carrito
        Optional<OrderItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Actualizar cantidad
            OrderItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            
            if (product.getStock() < newQuantity) {
                throw new Exception("Insufficient stock for total quantity. Available: " + product.getStock());
            }
            
            item.setQuantity(newQuantity);
            item.setSubtotal(product.getBasePrice().multiply(BigDecimal.valueOf(newQuantity)));
        } else {
            // Agregar nuevo item
            OrderItem newItem = OrderItem.builder()
                    .order(cart)
                    .product(product)
                    .productName(product.getName())
                    .productSku(product.getSku())
                    .quantity(quantity)
                    .unitPrice(product.getBasePrice())
                    .subtotal(product.getBasePrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();
            
            cart.getItems().add(newItem);
        }
        
        // Recalcular total
        recalculateTotal(cart);
        
        return repository.save(cart);
    }
    
    @Override
    @Transactional
    public Order updateCartItem(Long cartId, Long itemId, Integer quantity) throws Exception {
        Order cart = repository.findById(cartId)
                .orElseThrow(() -> new Exception("Cart not found"));
        
        if (cart.getStatus() != Order.OrderStatus.CART) {
            throw new Exception("Cannot modify a non-cart order");
        }
        
        OrderItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new Exception("Item not found in cart"));
        
        if (quantity <= 0) {
            // Eliminar item si cantidad es 0 o menor
            cart.getItems().remove(item);
        } else {
            // Verificar stock
            Product product = item.getProduct();
            if (product.getStock() < quantity) {
                throw new Exception("Insufficient stock. Available: " + product.getStock());
            }
            
            item.setQuantity(quantity);
            item.setSubtotal(product.getBasePrice().multiply(BigDecimal.valueOf(quantity)));
        }
        
        recalculateTotal(cart);
        return repository.save(cart);
    }
    
    @Override
    @Transactional
    public Order removeFromCart(Long cartId, Long itemId) throws Exception {
        Order cart = repository.findById(cartId)
                .orElseThrow(() -> new Exception("Cart not found"));
        
        if (cart.getStatus() != Order.OrderStatus.CART) {
            throw new Exception("Cannot modify a non-cart order");
        }
        
        OrderItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new Exception("Item not found in cart"));
        
        cart.getItems().remove(item);
        recalculateTotal(cart);
        
        return repository.save(cart);
    }
    
    @Override
    public Optional<Order> getActiveCart(Long userId) {
        return repository.findByUser_IdAndStatus(userId, Order.OrderStatus.CART);
    }
    
    @Override
    @Transactional
    public Order checkout(Long cartId, String shippingAddress, String notes) throws Exception {
        Order cart = repository.findById(cartId)
                .orElseThrow(() -> new Exception("Cart not found"));
        
        if (cart.getStatus() != Order.OrderStatus.CART) {
            throw new Exception("Cart is not active");
        }
        
        if (cart.getItems().isEmpty()) {
            throw new Exception("Cart is empty");
        }
        
        // Verificar stock de todos los productos
        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (!product.getActive()) {
                throw new Exception("Product " + product.getName() + " is not available");
            }
            if (product.getStock() < item.getQuantity()) {
                throw new Exception("Insufficient stock for " + product.getName() + ". Available: " + product.getStock());
            }
        }
        
        // Actualizar datos de envío
        cart.setShippingAddress(shippingAddress);
        cart.setNotes(notes);
        
        // Cambiar status a PENDING_PAYMENT
        cart.setStatus(Order.OrderStatus.PENDING_PAYMENT);
        
        return repository.save(cart);
    }
    
    @Override
    @Transactional
    public Order confirmPayment(Long orderId) throws Exception {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));
        
        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new Exception("Order is not pending payment");
        }
        
        // Reducir stock de cada producto
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            int updated = productRepository.reduceStock(product.getId(), item.getQuantity());
            
            if (updated == 0) {
                throw new Exception("Insufficient stock for product: " + product.getName());
            }
        }
        
        // Actualizar estado del pedido
        order.setStatus(Order.OrderStatus.PAID);
        
        return repository.save(order);
    }
    
    @Override
    @Transactional
    public Order cancelOrder(Long orderId) throws Exception {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));
        
        // Solo se puede cancelar si está en CART o PENDING_PAYMENT
        if (order.getStatus() != Order.OrderStatus.CART && 
            order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new Exception("Cannot cancel order in status: " + order.getStatus());
        }
        
        // Si ya estaba pagado, restaurar stock
        if (order.getStatus() == Order.OrderStatus.PAID) {
            for (OrderItem item : order.getItems()) {
                productRepository.increaseStock(item.getProduct().getId(), item.getQuantity());
            }
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        return repository.save(order);
    }
    
    // Método auxiliar para recalcular total
    private void recalculateTotal(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);
    }
}

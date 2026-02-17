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

import jakarta.validation.Valid;

import com.app.ventas_api.ventas.DTO.Request.PaymentRequestDto;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.Payment;
import com.app.ventas_api.ventas.IService.IPaymentService;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;

/**
 * VENTAS - Controller
 * PaymentController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/payments")
public class PaymentController {
    
    @Autowired
    private IPaymentService paymentService;
    
    @Autowired
    private IOrderRepository orderRepository;
    
    @GetMapping
    public ResponseEntity<List<Payment>> findAll() {
        try {
            List<Payment> payments = paymentService.all();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payment> findById(@PathVariable Long id) {
        try {
            Optional<Payment> payment = paymentService.findById(id);
            return payment.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> findByOrder(@PathVariable Long orderId) {
        try {
            Optional<Payment> payment = paymentService.findByOrderId(orderId);
            return payment.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> findByStatus(@PathVariable Payment.PaymentStatus status) {
        try {
            List<Payment> payments = paymentService.findByStatus(status);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/provider/{provider}")
    public ResponseEntity<List<Payment>> findByProvider(@PathVariable String provider) {
        try {
            List<Payment> payments = paymentService.findByProvider(provider);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody PaymentRequestDto request) {
        try {
            // Fetch the Order entity
            Order order = orderRepository.findById(request.getOrderId())
                    .orElse(null);
            
            Payment payment = Payment.builder()
                    .order(order)
                    .amount(request.getAmount())
                    .provider(request.getProvider())
                    .providerRef(request.getProviderRef())
                    .paymentMethod(request.getPaymentMethod())
                    .status(request.getStatus() != null ? request.getStatus() : Payment.PaymentStatus.PENDING)
                    .paidAt(request.getPaidAt())
                    .build();
            
            Payment saved = paymentService.save(payment);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @Valid @RequestBody PaymentRequestDto request) {
        try {
            // Fetch the Order entity
            Order order = orderRepository.findById(request.getOrderId())
                    .orElse(null);
            
            Payment payment = Payment.builder()
                    .order(order)
                    .amount(request.getAmount())
                    .provider(request.getProvider())
                    .providerRef(request.getProviderRef())
                    .paymentMethod(request.getPaymentMethod())
                    .status(request.getStatus())
                    .paidAt(request.getPaidAt())
                    .build();
            
            paymentService.update(id, payment);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            paymentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

package com.app.ventas_api.ventas.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.Payment;
import com.app.ventas_api.ventas.IRepository.IPaymentRepository;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.ventas.IService.IPaymentService;

/**
 * VENTAS - Service
 * Implementation: PaymentService
 */
@Service
public class PaymentService implements IPaymentService {
    
    @Autowired
    private IPaymentRepository repository;
    
    @Autowired
    private IOrderRepository orderRepository;
    
    @Override
    public List<Payment> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<Payment> findById(Long id) throws Exception {
        Optional<Payment> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Payment not found");
        }
        return op;
    }
    
    @Override
    public Payment save(Payment entity) throws Exception {
        try {
            // Fetch the Order entity to maintain the relationship
            if (entity.getOrder() != null && entity.getOrder().getId() != null) {
                Order order = orderRepository.findById(entity.getOrder().getId())
                        .orElseThrow(() -> new Exception("Order not found"));
                entity.setOrder(order);
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving payment: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Payment entity) throws Exception {
        Optional<Payment> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Payment not found");
        }
        
        Payment entityUpdate = op.get();
        entityUpdate.setOrder(entity.getOrder());
        entityUpdate.setAmount(entity.getAmount());
        entityUpdate.setProvider(entity.getProvider());
        entityUpdate.setProviderRef(entity.getProviderRef());
        entityUpdate.setPaymentMethod(entity.getPaymentMethod());
        entityUpdate.setStatus(entity.getStatus());
        entityUpdate.setPaidAt(entity.getPaidAt());
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<Payment> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Payment not found");
        }
        
        repository.delete(op.get());
    }
    
    @Override
    public Optional<Payment> findByOrderId(Long orderId) throws Exception {
        return repository.findByOrder_Id(orderId);
    }
    
    @Override
    public List<Payment> findByStatus(Payment.PaymentStatus status) {
        return repository.findByStatus(status);
    }
    
    @Override
    public List<Payment> findByProvider(String provider) {
        return repository.findByProvider(provider);
    }
}

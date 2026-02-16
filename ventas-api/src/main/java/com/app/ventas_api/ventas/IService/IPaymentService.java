package com.app.ventas_api.ventas.IService;

import java.util.List;
import java.util.Optional;

import com.app.ventas_api.ventas.domain.Payment;

/**
 * VENTAS - IService
 * Interface: IPaymentService
 */
public interface IPaymentService {
    
    List<Payment> all() throws Exception;
    
    Optional<Payment> findById(Long id) throws Exception;
    
    Payment save(Payment entity) throws Exception;
    
    void update(Long id, Payment entity) throws Exception;
    
    void delete(Long id) throws Exception;
    
    Optional<Payment> findByOrderId(Long orderId) throws Exception;
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByProvider(String provider);
}

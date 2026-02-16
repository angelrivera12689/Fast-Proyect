package com.app.ventas_api.ventas.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.ventas.domain.Payment;

import java.util.Optional;
import java.util.List;

/**
 * VENTAS - IRepository
 * Interface: IPaymentRepository
 */
@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrder_Id(Long orderId);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByProvider(String provider);
}

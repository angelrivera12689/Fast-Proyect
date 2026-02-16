package com.app.ventas_api.ventas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VENTAS - Domain
 * Entidad: Pago
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_payment_order"))
    @JsonIgnore
    private Order order;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String provider;
    
    @Column(name = "provider_ref")
    private String providerRef;
    
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}

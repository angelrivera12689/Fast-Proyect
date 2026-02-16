package com.app.ventas_api.ventas.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.app.ventas_api.ventas.domain.Payment;

/**
 * VENTAS - DTO Request
 * PaymentRequestDto
 */
@Data
public class PaymentRequestDto {
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Provider is required")
    private String provider;
    
    private String providerRef;
    
    private String paymentMethod;
    
    private Payment.PaymentStatus status;
    
    private LocalDateTime paidAt;
}

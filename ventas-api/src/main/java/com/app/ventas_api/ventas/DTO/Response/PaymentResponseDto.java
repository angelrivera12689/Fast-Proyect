package com.app.ventas_api.ventas.DTO.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.app.ventas_api.ventas.domain.Payment;

import lombok.Data;

/**
 * VENTAS - DTO Response
 * PaymentResponseDto
 */
@Data
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String provider;
    private String providerRef;
    private String paymentMethod;
    private Payment.PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}

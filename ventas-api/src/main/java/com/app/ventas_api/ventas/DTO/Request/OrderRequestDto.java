package com.app.ventas_api.ventas.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

import com.app.ventas_api.ventas.domain.Order;

/**
 * VENTAS - DTO Request
 * OrderRequestDto
 */
@Data
public class OrderRequestDto {
    
    @NotNull(message = "Company ID is required")
    private Long companyId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private BigDecimal totalAmount;
    
    private Order.OrderStatus status;
    
    private String shippingAddress;
    
    private String notes;
}

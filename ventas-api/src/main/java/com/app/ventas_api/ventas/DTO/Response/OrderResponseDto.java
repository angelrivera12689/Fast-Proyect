package com.app.ventas_api.ventas.DTO.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.OrderItem;

import lombok.Data;

/**
 * VENTAS - DTO Response
 * OrderResponseDto
 */
@Data
public class OrderResponseDto {
    private Long id;
    private Long companyId;
    private Long userId;
    private BigDecimal totalAmount;
    private Order.OrderStatus status;
    private String shippingAddress;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> items;
}

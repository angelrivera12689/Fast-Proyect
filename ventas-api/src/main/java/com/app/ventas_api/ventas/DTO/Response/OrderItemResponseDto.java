package com.app.ventas_api.ventas.DTO.Response;

import java.math.BigDecimal;

import lombok.Data;

/**
 * VENTAS - DTO Response
 * OrderItemResponseDto
 */
@Data
public class OrderItemResponseDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}

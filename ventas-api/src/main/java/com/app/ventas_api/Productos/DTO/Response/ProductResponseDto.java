package com.app.ventas_api.Productos.DTO.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * PRODUCTOS - DTO Response
 * ProductResponseDto
 */
@Data
public class ProductResponseDto {
    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String priceTiers;
    private Integer stock;
    private String sku;
    private BigDecimal weight;
    private String dimensions;
    private String imageUrl;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

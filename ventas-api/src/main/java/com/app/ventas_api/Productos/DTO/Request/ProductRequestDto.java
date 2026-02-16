package com.app.ventas_api.Productos.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PRODUCTOS - DTO Request
 * ProductRequestDto
 */
@Data
public class ProductRequestDto {
    
    private Long categoryId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
    
    private String priceTiers;
    
    @NotNull(message = "Stock is required")
    @Positive(message = "Stock must be positive")
    private Integer stock;
    
    private String sku;
    
    private BigDecimal weight;
    
    private String dimensions;
    
    private String imageUrl;
    
    private Boolean active;
}

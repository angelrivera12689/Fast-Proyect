package com.app.ventas_api.Productos.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * PRODUCTOS - DTO Request
 * CategoryRequestDto
 */
@Data
public class CategoryRequestDto {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    private Long parentId;
    
    private String imageUrl;
    
    private Boolean active;
}

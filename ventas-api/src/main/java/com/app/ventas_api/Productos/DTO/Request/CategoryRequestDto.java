package com.app.ventas_api.Productos.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * PRODUCTOS - DTO Request
 * CategoryRequestDto
 */
@Data
public class CategoryRequestDto {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;
    
    @Size(max = 500, message = "La descripción no debe exceder 500 caracteres")
    private String description;
    
    private Long parentId;
    
    @Size(max = 500, message = "La URL de la imagen no debe exceder 500 caracteres")
    private String imageUrl;
    
    private Boolean active;
}

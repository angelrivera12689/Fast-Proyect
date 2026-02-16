package com.app.ventas_api.seguridad.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SEGURIDAD - DTO Request
 * RoleRequestDto
 */
@Data
public class RoleRequestDto {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
}

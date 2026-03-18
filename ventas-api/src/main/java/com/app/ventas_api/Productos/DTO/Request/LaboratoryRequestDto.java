package com.app.ventas_api.Productos.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Laboratory Request DTO
 */
@Data
public class LaboratoryRequestDto {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
    private String name;
    
    @Size(max = 500, message = "La descripción no debe exceder 500 caracteres")
    private String description;
    
    @Size(min = 2, max = 100, message = "El país debe tener entre 2 y 100 caracteres")
    private String country;
    
    @Email(message = "El correo de contacto debe ser válido")
    @Size(max = 100, message = "El correo de contacto no debe exceder 100 caracteres")
    private String contactEmail;
    
    @Size(min = 7, max = 20, message = "El teléfono de contacto debe tener entre 7 y 20 caracteres")
    private String contactPhone;
    
    private Boolean active = true;
}

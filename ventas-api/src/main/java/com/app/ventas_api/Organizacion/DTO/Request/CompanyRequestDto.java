package com.app.ventas_api.Organizacion.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * ORGANIZACION - DTO Request
 * CompanyRequestDto
 */
@Data
public class CompanyRequestDto {
    
    @NotBlank(message = "El NIT es requerido")
    @Size(min = 5, max = 20, message = "El NIT debe tener entre 5 y 20 caracteres")
    private String nit;
    
    @NotBlank(message = "La razón social es requerida")
    @Size(min = 2, max = 200, message = "La razón social debe tener entre 2 y 200 caracteres")
    private String businessName;
    
    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 100, message = "El correo no debe exceder 100 caracteres")
    private String email;
    
    @Size(min = 7, max = 20, message = "El teléfono debe tener entre 7 y 20 caracteres")
    private String phone;
    
    @Size(max = 500, message = "La dirección no debe exceder 500 caracteres")
    private String address;
    
    @Size(max = 500, message = "La URL del logo no debe exceder 500 caracteres")
    private String logoUrl;
    
    private Boolean active;
}

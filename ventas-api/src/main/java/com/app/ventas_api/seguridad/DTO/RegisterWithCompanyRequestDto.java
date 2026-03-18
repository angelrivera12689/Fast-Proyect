package com.app.ventas_api.seguridad.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Authentication DTO - Register with Company Request
 */
@Data
public class RegisterWithCompanyRequestDto {
    
    @NotBlank(message = "El nombre de usuario es requerido")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;
    
    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico debe ser válido")
    private String email;
    
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "La contraseña debe contener al menos una letra mayúscula, una minúscula y un número"
    )
    private String password;
    
    @Size(min = 7, max = 20, message = "El teléfono debe tener entre 7 y 20 caracteres")
    private String phone;
    
    @Valid
    private CompanyDto company;
    
    /**
     * Nested DTO for company data
     */
    @Data
    public static class CompanyDto {
        
        @NotBlank(message = "El NIT es requerido")
        @Size(min = 5, max = 20, message = "El NIT debe tener entre 5 y 20 caracteres")
        private String nit;
        
        @NotBlank(message = "La razón social es requerida")
        @Size(min = 2, max = 200, message = "La razón social debe tener entre 2 y 200 caracteres")
        private String businessName;
        
        @NotBlank(message = "El correo de la empresa es requerido")
        @Email(message = "El correo de la empresa debe ser válido")
        private String email;
        
        @Size(min = 7, max = 20, message = "El teléfono debe tener entre 7 y 20 caracteres")
        private String phone;
        
        @Size(max = 500, message = "La dirección no debe exceder 500 caracteres")
        private String address;
        
        @Size(max = 500, message = "La URL del logo no debe exceder 500 caracteres")
        private String logoUrl;
    }
}

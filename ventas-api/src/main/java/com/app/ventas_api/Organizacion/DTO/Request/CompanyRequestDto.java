package com.app.ventas_api.Organizacion.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ORGANIZACION - DTO Request
 * CompanyRequestDto
 */
@Data
public class CompanyRequestDto {
    
    @NotBlank(message = "NIT is required")
    private String nit;
    
    @NotBlank(message = "Business name is required")
    private String businessName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    private String phone;
    
    private String address;
    
    private String logoUrl;
    
    private Boolean active;
}

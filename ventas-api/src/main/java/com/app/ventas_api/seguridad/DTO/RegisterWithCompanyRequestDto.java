package com.app.ventas_api.seguridad.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Authentication DTO - Register with Company Request
 */
@Data
public class RegisterWithCompanyRequestDto {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    private String phone;
    
    @Valid
    private CompanyDto company;
    
    /**
     * Nested DTO for company data
     */
    @Data
    public static class CompanyDto {
        
        @NotBlank(message = "NIT is required")
        private String nit;
        
        @NotBlank(message = "Business name is required")
        private String businessName;
        
        @NotBlank(message = "Company email is required")
        @Email(message = "Company email must be valid")
        private String email;
        
        private String phone;
        
        private String address;
        
        private String logoUrl;
    }
}

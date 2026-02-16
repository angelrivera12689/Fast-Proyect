package com.app.ventas_api.seguridad.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * SEGURIDAD - DTO Request
 * UserRequestDto
 */
@Data
public class UserRequestDto {
    
    private Long companyId;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    private String passwordHash;
    
    private String phone;
    
    private String avatarUrl;
    
    private Boolean twoFactorEnabled;
    
    private String twoFactorSecret;
    
    private String passwordResetToken;
    
    private java.time.LocalDateTime passwordResetExpires;
    
    private Boolean active;
}

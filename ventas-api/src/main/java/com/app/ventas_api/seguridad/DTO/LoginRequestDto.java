package com.app.ventas_api.seguridad.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Authentication DTO - Login Request
 */
@Data
public class LoginRequestDto {
    
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Código 2FA (opcional)
    private String twoFactorCode;
}

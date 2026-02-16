package com.app.ventas_api.seguridad.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Authentication DTO - Refresh Token Request
 */
@Data
public class RefreshTokenRequestDto {
    
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}

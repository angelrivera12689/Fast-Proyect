package com.app.ventas_api.seguridad.DTO.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * SEGURIDAD - DTO Request
 * RefreshTokenRequestDto
 */
@Data
public class RefreshTokenRequestDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Token is required")
    private String token;
    
    @NotNull(message = "Expires at is required")
    private LocalDateTime expiresAt;
    
    private Boolean revoked;
}

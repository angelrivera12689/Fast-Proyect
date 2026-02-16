package com.app.ventas_api.seguridad.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * SEGURIDAD - DTO Response
 * RefreshTokenResponseDto
 */
@Data
public class RefreshTokenResponseDto {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private Boolean revoked;
    private LocalDateTime createdAt;
}

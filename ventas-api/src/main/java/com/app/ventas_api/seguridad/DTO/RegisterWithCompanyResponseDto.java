package com.app.ventas_api.seguridad.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication DTO - Register with Company Response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterWithCompanyResponseDto {
    
    private String token;
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private String message;
    
    // Company data
    private Long companyId;
    private String companyName;
    private String companyNit;
}

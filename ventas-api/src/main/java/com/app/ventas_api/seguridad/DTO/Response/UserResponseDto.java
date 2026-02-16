package com.app.ventas_api.seguridad.DTO.Response;

import java.time.LocalDateTime;
import java.util.Set;

import com.app.ventas_api.seguridad.domain.Role;

import lombok.Data;

/**
 * SEGURIDAD - DTO Response
 * UserResponseDto
 */
@Data
public class UserResponseDto {
    private Long id;
    private Long companyId;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private Boolean twoFactorEnabled;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private Set<Role> roles;
}

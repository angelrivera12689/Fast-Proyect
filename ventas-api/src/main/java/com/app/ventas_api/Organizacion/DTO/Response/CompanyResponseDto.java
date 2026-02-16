package com.app.ventas_api.Organizacion.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * ORGANIZACION - DTO Response
 * CompanyResponseDto
 */
@Data
public class CompanyResponseDto {
    private Long id;
    private String nit;
    private String businessName;
    private String email;
    private String phone;
    private String address;
    private String logoUrl;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

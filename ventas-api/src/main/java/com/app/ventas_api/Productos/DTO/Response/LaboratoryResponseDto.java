package com.app.ventas_api.Productos.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Laboratory Response DTO
 */
@Data
public class LaboratoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String country;
    private String contactEmail;
    private String contactPhone;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

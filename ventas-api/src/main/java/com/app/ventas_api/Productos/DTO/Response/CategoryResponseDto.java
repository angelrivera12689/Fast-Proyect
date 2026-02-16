package com.app.ventas_api.Productos.DTO.Response;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * PRODUCTOS - DTO Response
 * CategoryResponseDto
 */
@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private String imageUrl;
    private Boolean active;
    private LocalDateTime createdAt;
}

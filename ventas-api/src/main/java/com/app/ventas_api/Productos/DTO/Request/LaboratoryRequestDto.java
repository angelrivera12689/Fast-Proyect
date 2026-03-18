package com.app.ventas_api.Productos.DTO.Request;

import lombok.Data;

/**
 * Laboratory Request DTO
 */
@Data
public class LaboratoryRequestDto {
    private String name;
    private String description;
    private String country;
    private String contactEmail;
    private String contactPhone;
    private Boolean active = true;
}

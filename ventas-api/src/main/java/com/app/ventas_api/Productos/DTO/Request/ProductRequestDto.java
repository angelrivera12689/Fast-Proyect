package com.app.ventas_api.Productos.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * PRODUCTOS - DTO Request
 * ProductRequestDto
 */
@Data
public class ProductRequestDto {
    
    private Long categoryId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
    
    private String priceTiers;
    
    @NotNull(message = "Stock is required")
    @Positive(message = "Stock must be positive")
    private Integer stock;
    
    private String sku;
    
    private BigDecimal weight;
    
    private String dimensions;
    
    // ===== Atributos de Medicamentos =====
    private String laboratory;  // Laboratorio
    private String registrationNumber;  // Registro INVIMA
    private String dosage;  // Dosis (500mg, 10ml)
    private LocalDate expirationDate;  // Fecha de vencimiento
    private String activeIngredient;  // Principio activo
    private String presentation;  // Presentación (tableta, jarabe)
    private Boolean requiresPrescription;  // Requiere receta
    // ===== Fin atributos medicamentos =====
    
    private String imageUrl;
    
    private Boolean active;
}

package com.app.ventas_api.Productos.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * PRODUCTOS - DTO Request
 * ProductRequestDto
 */
@Data
public class ProductRequestDto {
    
    @NotNull(message = "La categoría es requerida")
    @Positive(message = "El ID de categoría debe ser positivo")
    private Long categoryId;
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;
    
    @Size(max = 2000, message = "La descripción no debe exceder 2000 caracteres")
    private String description;
    
    @NotNull(message = "El precio base es requerido")
    @Positive(message = "El precio base debe ser positivo")
    private BigDecimal basePrice;
    
    @Size(max = 1000, message = "Los niveles de precio no deben exceder 1000 caracteres")
    private String priceTiers;
    
    @NotNull(message = "El stock es requerido")
    @Positive(message = "El stock debe ser positivo")
    private Integer stock;
    
    @Size(min = 3, max = 50, message = "El SKU debe tener entre 3 y 50 caracteres")
    private String sku;
    
    @Positive(message = "El peso debe ser positivo")
    private BigDecimal weight;
    
    @Size(max = 100, message = "Las dimensiones no deben exceder 100 caracteres")
    private String dimensions;
    
    // ===== Atributos de Medicamentos =====
    @Positive(message = "El ID del laboratorio debe ser positivo")
    private Long laboratoryId;  // ID del laboratorio
    private String laboratoryName;  // Nombre del laboratorio (para compatibilidad)
    
    @Size(max = 50, message = "El número de registro no debe exceder 50 caracteres")
    private String registrationNumber;  // Registro INVIMA
    
    @Size(max = 50, message = "La dosis no debe exceder 50 caracteres")
    private String dosage;  // Dosis (500mg, 10ml)
    
    @NotNull(message = "La fecha de vencimiento es requerida")
    private LocalDate expirationDate;  // Fecha de vencimiento
    
    @Size(max = 200, message = "El principio activo no debe exceder 200 caracteres")
    private String activeIngredient;  // Principio activo
    
    @Size(max = 50, message = "La presentación no debe exceder 50 caracteres")
    private String presentation;  // Presentación (tableta, jarabe)
    
    private Boolean requiresPrescription;  // Requiere receta
    // ===== Fin atributos medicamentos =====
    
    @Size(max = 500, message = "La URL de la imagen no debe exceder 500 caracteres")
    private String imageUrl;
    
    private Boolean active;
}

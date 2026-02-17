package com.app.ventas_api.Productos.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PRODUCTOS - Entity
 * Product
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    @JsonIgnore
    private Category category;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;
    
    @Column(columnDefinition = "JSON")
    private String priceTiers;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(unique = true)
    private String sku;
    
    private BigDecimal weight;
    
    private String dimensions;
    
    // ===== Atributos de Medicamentos =====
    @Column(name = "laboratory")
    private String laboratory;  // Laboratorio farmacéutico
    
    @Column(name = "registration_number")
    private String registrationNumber;  // Registro INVIMA
    
    @Column(name = "dosage")
    private String dosage;  // Dosis (ej: 500mg, 10ml)
    
    @Column(name = "expiration_date")
    private LocalDate expirationDate;  // Fecha de vencimiento
    
    @Column(name = "active_ingredient")
    private String activeIngredient;  // Principio activo
    
    @Column(name = "presentation")
    private String presentation;  // Presentación (tableta, jarabe, inyectable)
    
    @Column(name = "requires_prescription")
    @Builder.Default
    private Boolean requiresPrescription = false;  // Requiere receta
    
    // ===== Fin atributos medicamentos =====
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

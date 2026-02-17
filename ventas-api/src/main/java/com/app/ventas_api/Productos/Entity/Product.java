package com.app.ventas_api.Productos.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * PRODUCTOS - Entity
 * Product
 * 
 * Incluye auditoría automática con Spring Data JPA
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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
    
    // ===== Auditoría automática =====
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
}

package com.app.ventas_api.Productos.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * PRODUCTOS - Entity
 * Category
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    // Self-referencing relationship for parent category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_category_parent"))
    @JsonIgnore
    private Category parent;
    
    // Inverse relationship: a category can have many subcategories
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Category> children = new ArrayList<>();
    
    // Inverse relationship: a category can have many products
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private List<Product> products = new ArrayList<>();
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

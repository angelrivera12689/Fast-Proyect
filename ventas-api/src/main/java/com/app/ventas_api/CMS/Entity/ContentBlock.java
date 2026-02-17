package com.app.ventas_api.CMS.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * CMS - Entity
 * ContentBlock - Bloques de contenido editable
 * 
 * Secciones: homepage, about, contact, footer, policies, etc.
 * Keys: hero_title, hero_image, about_text, contact_email, etc.
 */
@Entity
@Table(name = "content_blocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ContentBlock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String section;  // homepage, about, contact, footer, policies
    
    @Column(name = "content_key", nullable = false)
    private String contentKey;  // hero_title, about_text, contact_email, banner_1, etc.
    
    @Column(columnDefinition = "TEXT")
    private String content;  // Texto del contenido
    
    @Column(name = "image_url")
    private String imageUrl;  // URL de imagen
    
    @Column(name = "video_url")
    private String videoUrl;  // URL de video
    
    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;
    
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

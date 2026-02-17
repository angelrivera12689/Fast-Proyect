package com.app.ventas_api.CMS.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ventas_api.CMS.Entity.ContentBlock;
import com.app.ventas_api.CMS.IService.IContentBlockService;

/**
 * CMS - Controller
 * ContentBlockController - Gestión de contenido dinámico
 * 
 * Secciones disponibles:
 * - homepage: Página principal
 * - about: Quiénes somos
 * - contact: Información de contacto
 * - footer: Contenido del pie de página
 * - policies: Políticas y términos
 * - banner: Banners promocionales
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/content")
public class ContentBlockController {
    
    @Autowired
    private IContentBlockService contentBlockService;
    
    // ===== Lectura - USER, COMPANY_ADMIN, ADMIN =====
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ContentBlock>> findAll() {
        try {
            List<ContentBlock> contents = contentBlockService.findAll();
            return ResponseEntity.ok(contents);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<ContentBlock> findById(@PathVariable Long id) {
        try {
            Optional<ContentBlock> content = contentBlockService.findById(id);
            return content.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/section/{section}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ContentBlock>> findBySection(@PathVariable String section) {
        try {
            List<ContentBlock> contents = contentBlockService.findBySection(section);
            return ResponseEntity.ok(contents);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/key/{key}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<ContentBlock> findByContentKey(@PathVariable String key) {
        try {
            ContentBlock content = contentBlockService.findByContentKey(key);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ===== Escritura - Solo ADMIN =====
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContentBlock> create(@RequestBody ContentBlockRequest request) {
        try {
            ContentBlock content = ContentBlock.builder()
                    .section(request.getSection())
                    .contentKey(request.getKey())
                    .content(request.getContent())
                    .imageUrl(request.getImageUrl())
                    .videoUrl(request.getVideoUrl())
                    .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                    .active(request.getActive() != null ? request.getActive() : true)
                    .build();
            
            ContentBlock saved = contentBlockService.save(content);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContentBlock> update(@PathVariable Long id, @RequestBody ContentBlockRequest request) {
        try {
            ContentBlock content = ContentBlock.builder()
                    .section(request.getSection())
                    .contentKey(request.getKey())
                    .content(request.getContent())
                    .imageUrl(request.getImageUrl())
                    .videoUrl(request.getVideoUrl())
                    .sortOrder(request.getSortOrder())
                    .active(request.getActive())
                    .build();
            
            contentBlockService.update(id, content);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            contentBlockService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DTO interno
    public static class ContentBlockRequest {
        private String section;
        private String key;
        private String content;
        private String imageUrl;
        private String videoUrl;
        private Integer sortOrder;
        private Boolean active;
        
        public String getSection() { return section; }
        public void setSection(String section) { this.section = section; }
        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getVideoUrl() { return videoUrl; }
        public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }
}

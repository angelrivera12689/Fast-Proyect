package com.app.ventas_api.Productos.Controller;

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

import jakarta.validation.Valid;

import com.app.ventas_api.Productos.Entity.ProductImage;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IService.IProductImageService;
import com.app.ventas_api.Productos.IRepository.IProductRepository;

/**
 * PRODUCTOS - Controller
 * ProductImageController - Gestión de múltiples imágenes por producto
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/product-images")
public class ProductImageController {
    
    @Autowired
    private IProductImageService productImageService;
    
    @Autowired
    private IProductRepository productRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ProductImage>> findAll() {
        try {
            List<ProductImage> images = productImageService.findAll();
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<ProductImage> findById(@PathVariable Long id) {
        try {
            Optional<ProductImage> image = productImageService.findById(id);
            return image.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<ProductImage>> findByProductId(@PathVariable Long productId) {
        try {
            List<ProductImage> images = productImageService.findByProductId(productId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductImage> create(@Valid @RequestBody ProductImageRequest request) {
        try {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            ProductImage image = ProductImage.builder()
                    .product(product)
                    .url(request.getUrl())
                    .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
                    .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                    .altText(request.getAltText())
                    .build();
            
            ProductImage saved = productImageService.save(image);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductImage> update(@PathVariable Long id, @Valid @RequestBody ProductImageRequest request) {
        try {
            ProductImage image = ProductImage.builder()
                    .url(request.getUrl())
                    .isPrimary(request.getIsPrimary())
                    .sortOrder(request.getSortOrder())
                    .altText(request.getAltText())
                    .build();
            
            productImageService.update(id, image);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productImageService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteByProduct(@PathVariable Long productId) {
        try {
            productImageService.deleteByProductId(productId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DTO interno
    public static class ProductImageRequest {
        private Long productId;
        private String url;
        private Boolean isPrimary;
        private Integer sortOrder;
        private String altText;
        
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public Boolean getIsPrimary() { return isPrimary; }
        public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public String getAltText() { return altText; }
        public void setAltText(String altText) { this.altText = altText; }
    }
}

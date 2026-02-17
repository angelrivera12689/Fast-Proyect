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

import com.app.ventas_api.Productos.DTO.Request.CategoryRequestDto;
import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.IService.ICategoryService;

/**
 * PRODUCTOS - Controller
 * CategoryController
 * 
 * Roles:
 * - USER, COMPANY_ADMIN, ADMIN: Ver categorías
 * - COMPANY_ADMIN, ADMIN: Crear, actualizar, eliminar
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/categories")
public class CategoryController {
    
    @Autowired
    private ICategoryService categoryService;
    
    // ===== Métodos de lectura - USER, COMPANY_ADMIN, ADMIN =====
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Category>> findAll() {
        try {
            List<Category> categories = categoryService.all();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Category>> findActive() {
        try {
            List<Category> categories = categoryService.findByStateTrue();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        try {
            Optional<Category> category = categoryService.findById(id);
            return category.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Category> findByName(@PathVariable String name) {
        try {
            Optional<Category> category = categoryService.findByName(name);
            return category.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/parent/{parentId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Category>> findByParent(@PathVariable Long parentId) {
        try {
            List<Category> categories = categoryService.findByParentId(parentId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ===== Métodos de escritura - Solo ADMIN =====
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequestDto request) {
        try {
            Category saved = categoryService.create(request.getName(), request.getDescription(), 
                request.getParentId(), request.getImageUrl(), request.getActive());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto request) {
        try {
            categoryService.updateCategory(id, request.getName(), request.getDescription(), 
                request.getParentId(), request.getImageUrl(), request.getActive());
            return ResponseEntity.ok(categoryService.findById(id).orElse(null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

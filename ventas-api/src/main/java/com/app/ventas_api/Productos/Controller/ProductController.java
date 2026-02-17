package com.app.ventas_api.Productos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.app.ventas_api.Productos.DTO.Request.ProductRequestDto;
import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IService.IProductService;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;

/**
 * PRODUCTOS - Controller
 * ProductController
 * 
 * Roles:
 * - USER, COMPANY_ADMIN, ADMIN: Ver productos
 * - COMPANY_ADMIN, ADMIN: Crear, actualizar, eliminar
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/products")
public class ProductController {
    
    @Autowired
    private IProductService productService;
    
    @Autowired
    private ICategoryRepository categoryRepository;
    
    // ===== Métodos de lectura - USER, COMPANY_ADMIN, ADMIN =====
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Product>> findAll() {
        try {
            List<Product> products = productService.all();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/products/paged - Listar productos con paginación
     * @param page Número de página (0-based)
     * @param size Tamaño de página
     * @param sortBy Campo para ordenar
     * @param sortDir Dirección de orden (ASC/DESC)
     */
    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<Product>> findAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            // Validar límites
            if (size > 100) size = 100; // Máximo 100 por página
            if (size < 1) size = 10;
            if (page < 0) page = 0;
            
            Sort sort = sortDir.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
            
            Page<Product> products = productService.findAll(PageRequest.of(page, size, sort));
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Product>> findActive() {
        try {
            List<Product> products = productService.findByStateTrue();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.findById(id);
            return product.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Product> findBySku(@PathVariable String sku) {
        try {
            Optional<Product> product = productService.findBySku(sku);
            return product.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Product>> findByCategory(@PathVariable Long categoryId) {
        try {
            List<Product> products = productService.findByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
        try {
            List<Product> products = productService.findByNameContaining(name);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // ===== Métodos de escritura - Solo ADMIN =====
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequestDto request) {
        try {
            // Fetch Category entity
            Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            
            Product product = Product.builder()
                    .category(category)
                    .name(request.getName())
                    .description(request.getDescription())
                    .basePrice(request.getBasePrice())
                    .priceTiers(request.getPriceTiers())
                    .stock(request.getStock())
                    .sku(request.getSku())
                    .weight(request.getWeight())
                    .dimensions(request.getDimensions())
                    // Atributos de medicamentos
                    .laboratory(request.getLaboratory())
                    .registrationNumber(request.getRegistrationNumber())
                    .dosage(request.getDosage())
                    .expirationDate(request.getExpirationDate())
                    .activeIngredient(request.getActiveIngredient())
                    .presentation(request.getPresentation())
                    .requiresPrescription(request.getRequiresPrescription() != null ? request.getRequiresPrescription() : false)
                    // Fin atributos
                    .imageUrl(request.getImageUrl())
                    .active(request.getActive() != null ? request.getActive() : true)
                    .build();
            
            Product saved = productService.save(product);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto request) {
        try {
            // Fetch Category entity
            Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);
            
            Product product = Product.builder()
                    .category(category)
                    .name(request.getName())
                    .description(request.getDescription())
                    .basePrice(request.getBasePrice())
                    .priceTiers(request.getPriceTiers())
                    .stock(request.getStock())
                    .sku(request.getSku())
                    .weight(request.getWeight())
                    .dimensions(request.getDimensions())
                    // Atributos de medicamentos
                    .laboratory(request.getLaboratory())
                    .registrationNumber(request.getRegistrationNumber())
                    .dosage(request.getDosage())
                    .expirationDate(request.getExpirationDate())
                    .activeIngredient(request.getActiveIngredient())
                    .presentation(request.getPresentation())
                    .requiresPrescription(request.getRequiresPrescription())
                    // Fin atributos
                    .imageUrl(request.getImageUrl())
                    .active(request.getActive())
                    .build();
            
            productService.update(id, product);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

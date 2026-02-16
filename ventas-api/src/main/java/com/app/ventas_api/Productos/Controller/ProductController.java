package com.app.ventas_api.Productos.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.app.ventas_api.Productos.DTO.Request.ProductRequestDto;
import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IService.IProductService;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;

/**
 * PRODUCTOS - Controller
 * ProductController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/products")
public class ProductController {
    
    @Autowired
    private IProductService productService;
    
    @Autowired
    private ICategoryRepository categoryRepository;
    
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        try {
            List<Product> products = productService.all();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Product>> findActive() {
        try {
            List<Product> products = productService.findByStateTrue();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
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
    public ResponseEntity<List<Product>> findByCategory(@PathVariable Long categoryId) {
        try {
            List<Product> products = productService.findByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchByName(@RequestParam String name) {
        try {
            List<Product> products = productService.findByNameContaining(name);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequestDto request) {
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
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody ProductRequestDto request) {
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

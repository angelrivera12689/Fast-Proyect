package com.app.ventas_api.seguridad.config;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.domain.Role;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IRepository.IRoleRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Data Initializer - Seeds default data on startup
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private IRoleRepository roleRepository;
    
    @Autowired
    private ICategoryRepository categoryRepository;
    
    @Autowired
    private IProductRepository productRepository;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("=== DataInitializer: Starting data initialization ===");
        
        // Seed default roles
        seedRoles();
        
        // Seed categories
        seedCategories();
        
        // Seed products
        seedProducts();
        
        // Seed company and user
        seedCompanyAndUser();
        
        logger.info("=== DataInitializer: Data initialization complete ===");
    }
    
    private void seedRoles() {
        List<String> defaultRoles = Arrays.asList("ADMIN", "USER", "MANAGER");
        
        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                logger.info("DataInitializer: Created role: {}", roleName);
            }
        }
    }
    
    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            // Categoría principal
            Category medicamentos = Category.builder()
                    .name("Medicamentos")
                    .description("Medicamentos y fármacos")
                    .active(true)
                    .build();
            categoryRepository.save(medicamentos);
            logger.info("DataInitializer: Created category: Medicamentos");
            
            Category analgesicos = Category.builder()
                    .name("Analgésicos")
                    .description("Analgésicos y antipiréticos")
                    .parent(medicamentos)
                    .active(true)
                    .build();
            categoryRepository.save(analgesicos);
            logger.info("DataInitializer: Created category: Analgésicos");
            
            Category vitamins = Category.builder()
                    .name("Vitaminas y Suplementos")
                    .description("Vitaminas y suplementos nutricionales")
                    .active(true)
                    .build();
            categoryRepository.save(vitamins);
            logger.info("DataInitializer: Created category: Vitaminas");
        }
    }
    
    private void seedProducts() {
        if (productRepository.count() == 0) {
            Category catAnalgesicos = categoryRepository.findByName("Analgésicos").orElse(null);
            Category catVitamins = categoryRepository.findByName("Vitaminas y Suplementos").orElse(null);
            
            if (catAnalgesicos != null) {
                Product paracetamol = Product.builder()
                        .name("Paracetamol 500mg")
                        .description("Analgésico y antipirético para dolor y fiebre")
                        .category(catAnalgesicos)
                        .basePrice(new BigDecimal("5000"))
                        .stock(100)
                        .sku("MED-001")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("10x5x2 cm")
                        .imageUrl("https://example.com/paracetamol.jpg")
                        .active(true)
                        .build();
                productRepository.save(paracetamol);
                logger.info("DataInitializer: Created product: Paracetamol 500mg");
                
                Product ibuprofeno = Product.builder()
                        .name("Ibuprofeno 400mg")
                        .description("Antiinflamatorio no esteroideo")
                        .category(catAnalgesicos)
                        .basePrice(new BigDecimal("6500"))
                        .stock(80)
                        .sku("MED-002")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("10x5x2 cm")
                        .active(true)
                        .build();
                productRepository.save(ibuprofeno);
                logger.info("DataInitializer: Created product: Ibuprofeno 400mg");
            }
            
            if (catVitamins != null) {
                Product multivitamin = Product.builder()
                        .name("Multivitamínico día")
                        .description("Complejo vitamínico para el daily")
                        .category(catVitamins)
                        .basePrice(new BigDecimal("25000"))
                        .stock(50)
                        .sku("VIT-001")
                        .active(true)
                        .build();
                productRepository.save(multivitamin);
                logger.info("DataInitializer: Created product: Multivitamínico");
            }
        }
    }
    
    private void seedCompanyAndUser() {
        if (companyRepository.count() == 0) {
            // Crear empresa
            Company empresa = Company.builder()
                    .nit("900123456-1")
                    .businessName("Farmacia La Salud SAS")
                    .email("contacto@farmacialasalud.com")
                    .phone("3001234567")
                    .address("Calle 45 #12-34, Bogotá")
                    .active(true)
                    .build();
            companyRepository.save(empresa);
            logger.info("DataInitializer: Created company: Farmacia La Salud SAS");
            
            // Crear usuario de prueba
            Role userRole = roleRepository.findByName("USER").orElse(null);
            
            if (userRole != null) {
                User usuario = User.builder()
                        .username("admin")
                        .email("admin@farmacialasalud.com")
                        .passwordHash(passwordEncoder.encode("admin123"))
                        .phone("3001234567")
                        .company(empresa)
                        .active(true)
                        .build();
                usuario.getRoles().add(userRole);
                userRepository.save(usuario);
                logger.info("DataInitializer: Created user: admin / admin123");
            }
        }
    }
}

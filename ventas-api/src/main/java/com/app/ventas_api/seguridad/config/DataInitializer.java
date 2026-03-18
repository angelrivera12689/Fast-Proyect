package com.app.ventas_api.seguridad.config;

import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Laboratory;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IRepository.ILaboratoryRepository;
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
    private ILaboratoryRepository laboratoryRepository;
    
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
        
        // Seed laboratories
        seedLaboratories();
        
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
            // Categoría principal: Medicamentos
            Category medicamentos = Category.builder()
                    .name("Medicamentos")
                    .description("Medicamentos y fármacos de prescripción")
                    .active(true)
                    .build();
            categoryRepository.save(medicamentos);
            logger.info("DataInitializer: Created category: Medicamentos");
            
            // Subcategorías de Medicamentos
            Category analgesicos = Category.builder()
                    .name("Analgésicos")
                    .description("Analgésicos y antipiréticos")
                    .parent(medicamentos)
                    .active(true)
                    .build();
            categoryRepository.save(analgesicos);
            logger.info("DataInitializer: Created category: Analgésicos");
            
            Category antibioticos = Category.builder()
                    .name("Antibióticos")
                    .description("Medicamentos antibacterianos")
                    .parent(medicamentos)
                    .active(true)
                    .build();
            categoryRepository.save(antibioticos);
            logger.info("DataInitializer: Created category: Antibióticos");
            
            Category antiinflamatorios = Category.builder()
                    .name("Antiinflamatorios")
                    .description("Medicamentos antiinflamatorios")
                    .parent(medicamentos)
                    .active(true)
                    .build();
            categoryRepository.save(antiinflamatorios);
            logger.info("DataInitializer: Created category: Antiinflamatorios");
            
            // Vitaminas y Suplementos
            Category vitamins = Category.builder()
                    .name("Vitaminas y Suplementos")
                    .description("Vitaminas y suplementos nutricionales")
                    .active(true)
                    .build();
            categoryRepository.save(vitamins);
            logger.info("DataInitializer: Created category: Vitaminas");
            
            // Cuidado Personal
            Category cuidadoPersonal = Category.builder()
                    .name("Cuidado Personal")
                    .description("Productos de cuidado personal")
                    .active(true)
                    .build();
            categoryRepository.save(cuidadoPersonal);
            logger.info("DataInitializer: Created category: Cuidado Personal");
        }
    }
    
    private void seedLaboratories() {
        if (laboratoryRepository.count() == 0) {
            // Laboratorios farmacéuticos
            Laboratory genfar = Laboratory.builder()
                    .name("Genfar")
                    .description("Laboratorio farmacéutico colombiano de referencia")
                    .country("Colombia")
                    .active(true)
                    .build();
            laboratoryRepository.save(genfar);
            logger.info("DataInitializer: Created laboratory: Genfar");
            
            Laboratory bayer = Laboratory.builder()
                    .name("Bayer")
                    .description("Corporación farmacéutica alemana multinational")
                    .country("Alemania")
                    .active(true)
                    .build();
            laboratoryRepository.save(bayer);
            logger.info("DataInitializer: Created laboratory: Bayer");
            
            Laboratory pfizer = Laboratory.builder()
                    .name("Pfizer")
                    .description("Corporación farmacéutica estadounidense")
                    .country("Estados Unidos")
                    .active(true)
                    .build();
            laboratoryRepository.save(pfizer);
            logger.info("DataInitializer: Created laboratory: Pfizer");
            
            Laboratory laSante = Laboratory.builder()
                    .name("La Santé")
                    .description("Laboratorio farmacéutico colombiano")
                    .country("Colombia")
                    .active(true)
                    .build();
            laboratoryRepository.save(laSante);
            logger.info("DataInitializer: Created laboratory: La Santé");
            
            Laboratory mk = Laboratory.builder()
                    .name("MK")
                    .description("Laboratorio farmacéutico chileno")
                    .country("Chile")
                    .active(true)
                    .build();
            laboratoryRepository.save(mk);
            logger.info("DataInitializer: Created laboratory: MK");
            
            Laboratory genven = Laboratory.builder()
                    .name("Genven")
                    .description("Laboratorio farmacéutico venezolano")
                    .country("Venezuela")
                    .active(true)
                    .build();
            laboratoryRepository.save(genven);
            logger.info("DataInitializer: Created laboratory: Genven");
        }
    }
    
    private void seedProducts() {
        if (productRepository.count() == 0) {
            // Obtener categorías
            Category catAnalgesicos = categoryRepository.findByName("Analgésicos").orElse(null);
            Category catAntibioticos = categoryRepository.findByName("Antibióticos").orElse(null);
            Category catAntiinflamatorios = categoryRepository.findByName("Antiinflamatorios").orElse(null);
            Category catVitamins = categoryRepository.findByName("Vitaminas y Suplementos").orElse(null);
            Category catCuidadoPersonal = categoryRepository.findByName("Cuidado Personal").orElse(null);
            
            // Obtener laboratorios
            Laboratory labGenfar = laboratoryRepository.findByNameIgnoreCase("Genfar").orElse(null);
            Laboratory labBayer = laboratoryRepository.findByNameIgnoreCase("Bayer").orElse(null);
            Laboratory labPfizer = laboratoryRepository.findByNameIgnoreCase("Pfizer").orElse(null);
            Laboratory labLaSante = laboratoryRepository.findByNameIgnoreCase("La Santé").orElse(null);
            Laboratory labMk = laboratoryRepository.findByNameIgnoreCase("MK").orElse(null);
            Laboratory labGenven = laboratoryRepository.findByNameIgnoreCase("Genven").orElse(null);
            
            // ==== PRODUCTOS GENFAR ====
            if (catAnalgesicos != null) {
                Product genfarParacetamol = Product.builder()
                        .name("Genfar Paracetamol 500mg x 100 Tablets")
                        .description("Analgésico y antipirético. Alivia el dolor y la fiebre.")
                        .category(catAnalgesicos)
                        .laboratory(labGenfar)
                        .basePrice(new BigDecimal("12500"))
                        .stock(500)
                        .sku("GEN-PAR-500")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("5x10x2 cm")
                        .dosage("500mg")
                        .activeIngredient("Paracetamol")
                        .presentation("Tabletas")
                        .registrationNumber("INVIMA 2019-0000XXX-R1")
                        .active(true)
                        .build();
                productRepository.save(genfarParacetamol);
                logger.info("DataInitializer: Created product: Genfar Paracetamol 500mg");
            }
            
            if (catAntiinflamatorios != null) {
                Product genfarIbuprofeno = Product.builder()
                        .name("Genfar Ibuprofeno 400mg x 30 Cápsulas")
                        .description("Antiinflamatorio no esteroideo (AINE). Alivia el dolor, inflamación y fiebre.")
                        .category(catAntiinflamatorios)
                        .laboratory(labGenfar)
                        .basePrice(new BigDecimal("8900"))
                        .stock(350)
                        .sku("GEN-IBU-400")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("5x10x2 cm")
                        .dosage("400mg")
                        .activeIngredient("Ibuprofeno")
                        .presentation("Cápsulas")
                        .registrationNumber("INVIMA 2018-0000XXX-R1")
                        .active(true)
                        .build();
                productRepository.save(genfarIbuprofeno);
                logger.info("DataInitializer: Created product: Genfar Ibuprofeno 400mg");
            }
            
            if (catAntibioticos != null) {
                Product genfarAmoxicilina = Product.builder()
                        .name("Genfar Amoxicilina 500mg x 21 Cápsulas")
                        .description("Antibiótico de amplio espectro para infecciones bacterianas.")
                        .category(catAntibioticos)
                        .laboratory(labGenfar)
                        .basePrice(new BigDecimal("15800"))
                        .stock(200)
                        .sku("GEN-AMO-500")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("5x10x2 cm")
                        .dosage("500mg")
                        .activeIngredient("Amoxicilina")
                        .presentation("Cápsulas")
                        .registrationNumber("INVIMA 2017-0000XXX-R1")
                        .requiresPrescription(true)
                        .active(true)
                        .build();
                productRepository.save(genfarAmoxicilina);
                logger.info("DataInitializer: Created product: Genfar Amoxicilina 500mg");
            }
            
            if (catVitamins != null) {
                Product genfarMultivitaminico = Product.builder()
                        .name("Genfar Multivitamínico x 30 Tabletas")
                        .description("Complejo vitamínico y mineral para deficiencias nutricionales.")
                        .category(catVitamins)
                        .laboratory(labGenfar)
                        .basePrice(new BigDecimal("22000"))
                        .stock(400)
                        .sku("GEN-MULT-30")
                        .weight(new BigDecimal("0.08"))
                        .dimensions("6x11x3 cm")
                        .activeIngredient("Vitaminas A, B, C, D, E, Minerales")
                        .presentation("Tabletas")
                        .active(true)
                        .build();
                productRepository.save(genfarMultivitaminico);
                logger.info("DataInitializer: Created product: Genfar Multivitamínico");
                
                Product genfarVitaminaC = Product.builder()
                        .name("Genfar Vitamina C 1000mg x 30 Tabletas Efervescentes")
                        .description("Suplemento de vitamina C para reforzar el sistema inmune.")
                        .category(catVitamins)
                        .laboratory(labGenfar)
                        .basePrice(new BigDecimal("18500"))
                        .stock(300)
                        .sku("GEN-VITC-1000")
                        .weight(new BigDecimal("0.06"))
                        .dimensions("6x11x3 cm")
                        .dosage("1000mg")
                        .activeIngredient("Ácido Ascórbico")
                        .presentation("Tabletas Efervescentes")
                        .active(true)
                        .build();
                productRepository.save(genfarVitaminaC);
                logger.info("DataInitializer: Created product: Genfar Vitamina C 1000mg");
            }
            
            // ==== PRODUCTOS BAYER ====
            if (catAnalgesicos != null) {
                Product bayerAspirina = Product.builder()
                        .name("Bayer Aspirina 500mg x 100 Tabletas")
                        .description("Analgésico, antipirético y antiagregante plaquetario.")
                        .category(catAnalgesicos)
                        .laboratory(labBayer)
                        .basePrice(new BigDecimal("18900"))
                        .stock(250)
                        .sku("BAY-ASP-500")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("5x10x2 cm")
                        .dosage("500mg")
                        .activeIngredient("Ácido Acetilsalicílico")
                        .presentation("Tabletas")
                        .active(true)
                        .build();
                productRepository.save(bayerAspirina);
                logger.info("DataInitializer: Created product: Bayer Aspirina 500mg");
            }
            
            if (catAntiinflamatorios != null) {
                Product bayerAleve = Product.builder()
                        .name("Bayer Aleve 220mg x 24 Cápsulas")
                        .description("Antiinflamatorio no esteroideo para alivio del dolor y inflamación.")
                        .category(catAntiinflamatorios)
                        .laboratory(labBayer)
                        .basePrice(new BigDecimal("22500"))
                        .stock(180)
                        .sku("BAY-ALV-220")
                        .weight(new BigDecimal("0.05"))
                        .dimensions("5x10x2 cm")
                        .dosage("220mg")
                        .activeIngredient("Naproxeno")
                        .presentation("Cápsulas")
                        .active(true)
                        .build();
                productRepository.save(bayerAleve);
                logger.info("DataInitializer: Created product: Bayer Aleve 220mg");
            }
            
            // ==== PRODUCTOS PFIZER ====
            if (catAntibioticos != null) {
                Product pfizerZithromax = Product.builder()
                        .name("Pfizer Zithromax 500mg x 3 Tabletas")
                        .description("Antibiótico macrólido para infecciones respiratorias y de piel.")
                        .category(catAntibioticos)
                        .laboratory(labPfizer)
                        .basePrice(new BigDecimal("45000"))
                        .stock(100)
                        .sku("PFZ-ZITH-500")
                        .weight(new BigDecimal("0.02"))
                        .dimensions("3x8x1 cm")
                        .dosage("500mg")
                        .activeIngredient("Azitromicina")
                        .presentation("Tabletas")
                        .registrationNumber("INVIMA 2016-0000XXX-R1")
                        .requiresPrescription(true)
                        .active(true)
                        .build();
                productRepository.save(pfizerZithromax);
                logger.info("DataInitializer: Created product: Pfizer Zithromax 500mg");
            }
            
            // ==== PRODUCTOS LA SANTE ====
            if (catVitamins != null) {
                Product laSanteCalcimed = Product.builder()
                        .name("La Santé Calcimed Calcium + Vitamin D3 x 60 Tabletas")
                        .description("Suplemento de calcio con vitamina D3 para huesos fuertes.")
                        .category(catVitamins)
                        .laboratory(labLaSante)
                        .basePrice(new BigDecimal("32000"))
                        .stock(200)
                        .sku("LS-CALC-60")
                        .weight(new BigDecimal("0.12"))
                        .dimensions("7x12x4 cm")
                        .activeIngredient("Carbonato de Calcio, Vitamina D3")
                        .presentation("Tabletas")
                        .active(true)
                        .build();
                productRepository.save(laSanteCalcimed);
                logger.info("DataInitializer: Created product: La Santé Calcimed");
            }
            
            // ==== PRODUCTOS MK ====
            if (catCuidadoPersonal != null) {
                Product mkCepillo = Product.builder()
                        .name("MK Cepillo Dental Suave x 1")
                        .description("Cepillo dental con cerdas suaves para limpieza efectiva.")
                        .category(catCuidadoPersonal)
                        .laboratory(labMk)
                        .basePrice(new BigDecimal("5500"))
                        .stock(500)
                        .sku("MK-CEP-01")
                        .weight(new BigDecimal("0.02"))
                        .dimensions("2x20x2 cm")
                        .presentation("Unidad")
                        .active(true)
                        .build();
                productRepository.save(mkCepillo);
                logger.info("DataInitializer: Created product: MK Cepillo Dental");
                
                Product mkEnjuague = Product.builder()
                        .name("MK Enjuague Bucal Mentol x 250ml")
                        .description("Enjuague bucal para frescura y protección antibacterial.")
                        .category(catCuidadoPersonal)
                        .laboratory(labMk)
                        .basePrice(new BigDecimal("12800"))
                        .stock(300)
                        .sku("MK-ENJ-250")
                        .weight(new BigDecimal("0.28"))
                        .dimensions("5x15x5 cm")
                        .presentation("Frasco")
                        .active(true)
                        .build();
                productRepository.save(mkEnjuague);
                logger.info("DataInitializer: Created product: MK Enjuague Bucal");
            }
            
            // ==== PRODUCTOS GENVEN ====
            if (catCuidadoPersonal != null) {
                Product genvenShampoo = Product.builder()
                        .name("Genven Shampoo Anticaspa x 350ml")
                        .description("Shampoo tratante para eliminar la caspa y controlar la seborrea.")
                        .category(catCuidadoPersonal)
                        .laboratory(labGenven)
                        .basePrice(new BigDecimal("18500"))
                        .stock(250)
                        .sku("GNV-SHAM-350")
                        .weight(new BigDecimal("0.38"))
                        .dimensions("6x18x6 cm")
                        .presentation("Frasco")
                        .active(true)
                        .build();
                productRepository.save(genvenShampoo);
                logger.info("DataInitializer: Created product: Genven Shampoo Anticaspa");
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
                // Buscar si el usuario admin ya existe
                User usuario = userRepository.findByUsername("admin").orElse(null);
                if (usuario == null) {
                    usuario = User.builder()
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
                
                // Buscar si el usuario angelfaridr1 ya existe
                User angelfarid = userRepository.findByUsername("angelfaridr1").orElse(null);
                if (angelfarid == null) {
                    angelfarid = User.builder()
                            .username("angelfaridr1")
                            .email("angelfaridr1@gmail.com")
                            .passwordHash(passwordEncoder.encode("password123"))
                            .phone("3009876543")
                            .company(empresa)
                            .active(true)
                            .twoFactorEnabled(false)
                            .build();
                    angelfarid.getRoles().add(userRole);
                    userRepository.save(angelfarid);
                    logger.info("DataInitializer: Created user: angelfaridr1 / password123");
                } else {
                    // Actualizar 2FA si el usuario ya existe
                    angelfarid.setTwoFactorEnabled(false);
                    userRepository.save(angelfarid);
                    logger.info("DataInitializer: Updated user angelfaridr1 with 2FA disabled");
                }
            }
        }
    }
}

package com.app.ventas_api.seguridad.config;

import com.app.ventas_api.seguridad.domain.Role;
import com.app.ventas_api.seguridad.IRepository.IRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Data Initializer - Seeds default roles on startup
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private IRoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("=== DataInitializer: Starting role initialization ===");
        // Seed default roles
        List<String> defaultRoles = Arrays.asList("ADMIN", "USER", "MANAGER");
        
        for (String roleName : defaultRoles) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                logger.info("DataInitializer: Created role: {}", roleName);
            } else {
                logger.info("DataInitializer: Role already exists: {}", roleName);
            }
        }
        logger.info("=== DataInitializer: Role initialization complete ===");
    }
}

package com.app.ventas_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuración de Auditoría con Spring Data JPA
 * 
 * Para usar en entidades:
 * - Agregar @EntityListeners(AuditingEntityListener.class)
 * - Agregar @CreatedBy y @LastModifiedBy
 * - Agregar @CreatedDate y @LastModifiedDate
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * Proveedor de auditoría que obtiene el usuario actual
     * desde el contexto de seguridad
     */
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() 
                    || "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of("SYSTEM");
            }
            
            return Optional.of(authentication.getName());
        };
    }
}

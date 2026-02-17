package com.app.ventas_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI
 * 
 * Accede a la documentación en: /swagger-ui.html
 * Documentación JSON: /v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ventas API - MicroFarma B2B")
                        .version("1.0.0")
                        .description("""
                                API REST para sistema de ventas B2B de farmacia.
                                
                                ## Características:
                                - Autenticación JWT con Refresh Tokens
                                - 2FA con Google Authenticator
                                - Gestión de empresas y usuarios
                                - Catálogo de productos
                                - Pedidos y cotizaciones
                                - Pagos con MercadoPago
                                
                                ## Roles:
                                - **ADMIN**: Acceso total
                                - **COMPANY_ADMIN**: Gestión de empresa
                                - **USER**: Comprador
                                """)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("soporte@microfarma.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT obtenido del endpoint /api/auth/login")));
    }
}

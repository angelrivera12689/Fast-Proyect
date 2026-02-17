package com.app.ventas_api.seguridad.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration
 * 
 * Reglas de acceso:
 * - /api/auth/** - Público (login, register, refresh token, forgot password)
 * - /api/products/** - Autenticado (USER, COMPANY_ADMIN, ADMIN)
 * - /api/categories/** - Autenticado (USER, COMPANY_ADMIN, ADMIN)
 * - /api/orders/** - Autenticado (USER, COMPANY_ADMIN, ADMIN)
 * - /api/companies/** - ADMIN y COMPANY_ADMIN
 * - /api/users/** - Solo ADMIN
 * - /api/roles/** - Solo ADMIN
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configure(http))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentTypeOptions(content -> {})
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data: https:; font-src 'self' data:;")
                )
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos - autenticación
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                // Swagger/OpenAPI
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                
                // Endpoints solo ADMIN
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                
                // Endpoints ADMIN y COMPANY_ADMIN
                .requestMatchers("/api/companies/**").hasAnyRole("ADMIN", "COMPANY_ADMIN")
                
                // Endpoints para USER, COMPANY_ADMIN, ADMIN (productos, pedidos, etc)
                .requestMatchers("/api/products/**").hasAnyRole("USER", "COMPANY_ADMIN", "ADMIN")
                .requestMatchers("/api/categories/**").hasAnyRole("USER", "COMPANY_ADMIN", "ADMIN")
                .requestMatchers("/api/orders/**").hasAnyRole("USER", "COMPANY_ADMIN", "ADMIN")
                .requestMatchers("/api/order-items/**").hasAnyRole("USER", "COMPANY_ADMIN", "ADMIN")
                .requestMatchers("/api/payments/**").hasAnyRole("USER", "COMPANY_ADMIN", "ADMIN")
                
                // Cualquier otra request requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

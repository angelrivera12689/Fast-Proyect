package com.app.ventas_api.seguridad.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * SEGURIDAD - Domain
 * Entidad: Rol
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    private String description;
}

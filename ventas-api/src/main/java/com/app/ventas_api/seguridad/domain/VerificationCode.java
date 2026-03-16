package com.app.ventas_api.seguridad.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar códigos de verificación (2FA, recuperación de contraseña)
 */
@Entity
@Table(name = "verification_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_verification_user"))
    private User user;

    @Column(nullable = false)
    private String code;

    @Column(name = "code_type")
    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "used")
    @Builder.Default
    private Boolean used = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        return !used && !isExpired();
    }

    public enum CodeType {
        PASSWORD_RESET,  // Recuperación de contraseña
        LOGIN_2FA       // Verificación en dos pasos para login
    }
}

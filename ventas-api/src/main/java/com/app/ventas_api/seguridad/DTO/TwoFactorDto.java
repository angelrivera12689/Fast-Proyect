package com.app.ventas_api.seguridad.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTOs para autenticación de dos factores (2FA)
 */
public class TwoFactorDto {

    /**
     * Request para habilitar 2FA
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EnableTwoFactorRequest {
        private String code; // Código de verificación inicial
    }

    /**
     * Request para validar código 2FA
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VerifyTwoFactorRequest {
        private String code; // Código de 6 dígitos
    }

    /**
     * Response con la información del QR y secreto
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SetupTwoFactorResponse {
        private String secret;
        private String qrCodeImage; // Imagen QR en base64
        private String message;
    }

    /**
     * Response genérico para operaciones 2FA
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TwoFactorResponse {
        private boolean success;
        private String message;
        private Boolean twoFactorEnabled;
    }
}

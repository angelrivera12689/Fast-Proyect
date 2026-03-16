package com.app.ventas_api.seguridad.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Authentication DTO - Reset Password Request
 * Ahora usa código de verificación en lugar de token
 */
@Data
public class ResetPasswordDto {
    
    @NotBlank(message = "El correo es requerido")
    @Email(message = "Correo inválido")
    private String email;
    
    @NotBlank(message = "El código de verificación es requerido")
    @Size(min = 6, max = 6, message = "El código debe tener 6 dígitos")
    private String code;
    
    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String newPassword;
}

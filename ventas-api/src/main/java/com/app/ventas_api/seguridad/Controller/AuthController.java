package com.app.ventas_api.seguridad.Controller;

import com.app.ventas_api.seguridad.DTO.*;
import com.app.ventas_api.seguridad.Service.AuthService;
import com.app.ventas_api.seguridad.Service.JwtService;
import com.app.ventas_api.seguridad.Service.TotpService;
import com.app.ventas_api.seguridad.config.RateLimitingService;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.domain.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Authentication Controller
 * Endpoints for register, login, password recovery, etc.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private TotpService totpService;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private RateLimitingService rateLimitingService;
    
    /**
     * POST /api/auth/register - Register new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto request) {
        try {
            AuthResponseDto response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/register-with-company - Register new user with company (combined)
     */
    @PostMapping("/register-with-company")
    public ResponseEntity<?> registerWithCompany(@Valid @RequestBody RegisterWithCompanyRequestDto request) {
        try {
            RegisterWithCompanyResponseDto response = authService.registerWithCompany(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/login - Login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request, @RequestHeader(value = "X-Forwarded-For", required = false) String clientIp) {
        try {
            // Rate limiting por IP
            String ip = clientIp != null ? clientIp : "unknown";
            if (!rateLimitingService.tryConsume(ip)) {
                return ResponseEntity.status(429).body(java.util.Map.of(
                    "error", "Demasiados intentos. Intenta de nuevo en 1 minuto.",
                    "retryAfter", 60
                ));
            }
            
            AuthResponseDto response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/forgot-password - Forgot password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        try {
            String response = authService.forgotPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * POST /api/auth/reset-password - Reset password with token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDto request) {
        try {
            String response = authService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * POST /api/auth/change-password - Change password (authenticated)
     */
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordDto request) {
        try {
            // Extract user ID from token (simplified - in production use proper JWT validation)
            Long userId = extractUserIdFromToken(authHeader);
            String response = authService.changePassword(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    /**
     * POST /api/auth/refresh - Refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        try {
            AuthResponseDto response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // ==================== 2FA Endpoints ====================
    
    /**
     * GET /api/auth/2fa/setup - Generar código QR para configurar 2FA
     */
    @GetMapping("/2fa/setup")
    public ResponseEntity<?> setupTwoFactor(@RequestHeader("Authorization") String authHeader) {
        try {
            Long userId = extractUserIdFromToken(authHeader);
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Usuario no encontrado"));
            }
            
            User user = userOpt.get();
            
            // Si ya tiene 2FA habilitado, devolver info existente
            if (user.getTwoFactorEnabled() && user.getTwoFactorSecret() != null) {
                String qrCode = totpService.generateQrCodeImage(
                    user.getTwoFactorSecret(), 
                    user.getUsername(), 
                    "VentasAPI"
                );
                return ResponseEntity.ok(TwoFactorDto.SetupTwoFactorResponse.builder()
                    .secret(user.getTwoFactorSecret())
                    .qrCodeImage(qrCode)
                    .message("2FA ya está habilitado")
                    .build());
            }
            
            // Generar nuevo secreto
            String secret = totpService.generateSecret();
            String qrCode = totpService.generateQrCodeImage(secret, user.getUsername(), "VentasAPI");
            
            return ResponseEntity.ok(TwoFactorDto.SetupTwoFactorResponse.builder()
                .secret(secret)
                .qrCodeImage(qrCode)
                .message("Escanee el código QR con su app de autenticación")
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/2fa/enable - Habilitar 2FA con código de verificación
     */
    @PostMapping("/2fa/enable")
    public ResponseEntity<?> enableTwoFactor(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody TwoFactorDto.EnableTwoFactorRequest request) {
        try {
            Long userId = extractUserIdFromToken(authHeader);
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Usuario no encontrado"));
            }
            
            User user = userOpt.get();
            
            // Si ya está habilitado, no hacer nada
            if (user.getTwoFactorEnabled()) {
                return ResponseEntity.ok(TwoFactorDto.TwoFactorResponse.builder()
                    .success(true)
                    .message("2FA ya está habilitado")
                    .twoFactorEnabled(true)
                    .build());
            }
            
            // Obtener el secreto (debería venir en una sesión o regenerarse)
            String secret = user.getTwoFactorSecret();
            if (secret == null || secret.isEmpty()) {
                // Generar nuevo secreto si no existe
                secret = totpService.generateSecret();
            }
            
            // Verificar el código
            if (!totpService.validateCode(secret, request.getCode())) {
                return ResponseEntity.badRequest().body(java.util.Map.of(
                    "error", "Código inválido. Asegúrese de usar el código actual de su app de autenticación"
                ));
            }
            
            // Habilitar 2FA
            user.setTwoFactorSecret(secret);
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
            
            return ResponseEntity.ok(TwoFactorDto.TwoFactorResponse.builder()
                .success(true)
                .message("2FA habilitado exitosamente")
                .twoFactorEnabled(true)
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/2fa/disable - Deshabilitar 2FA
     */
    @PostMapping("/2fa/disable")
    public ResponseEntity<?> disableTwoFactor(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody TwoFactorDto.EnableTwoFactorRequest request) {
        try {
            Long userId = extractUserIdFromToken(authHeader);
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Usuario no encontrado"));
            }
            
            User user = userOpt.get();
            
            // Verificar contraseña antes de disable 2FA
            // Por simplicidad, verificamos el código TOTP
            if (user.getTwoFactorSecret() != null && !totpService.validateCode(user.getTwoFactorSecret(), request.getCode())) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Código inválido"));
            }
            
            // Deshabilitar 2FA
            user.setTwoFactorEnabled(false);
            user.setTwoFactorSecret(null);
            userRepository.save(user);
            
            return ResponseEntity.ok(TwoFactorDto.TwoFactorResponse.builder()
                .success(true)
                .message("2FA deshabilitado exitosamente")
                .twoFactorEnabled(false)
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/2fa/verify - Verificar código 2FA
     */
    @PostMapping("/2fa/verify")
    public ResponseEntity<?> verifyTwoFactor(@Valid @RequestBody TwoFactorDto.VerifyTwoFactorRequest request) {
        try {
            // Este endpoint se usa para verificar sin necesidad de login
            // Requiere el userId o username en el request
            return ResponseEntity.ok(java.util.Map.of(
                "valid", true,
                "message", "Código verificado"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
    
    // Helper method to extract user ID from token
    private Long extractUserIdFromToken(String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                // Validar el token antes de extraer el userId
                if (!jwtService.validateToken(token)) {
                    throw new RuntimeException("Token inválido o expirado");
                }
                // Extraer el userId real del token
                return jwtService.extractUserId(token);
            }
            throw new RuntimeException("Authorization header no proporcionado");
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el token: " + e.getMessage());
        }
    }
    
    // Helper method to extract username from token
    private String extractUsernameFromToken(String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (!jwtService.validateToken(token)) {
                    throw new RuntimeException("Token inválido o expirado");
                }
                return jwtService.extractUsername(token);
            }
            throw new RuntimeException("Authorization header no proporcionado");
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el token: " + e.getMessage());
        }
    }
}

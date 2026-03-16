package com.app.ventas_api.seguridad.Service;

import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.domain.RefreshToken;
import com.app.ventas_api.seguridad.domain.Role;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.domain.VerificationCode;
import com.app.ventas_api.seguridad.domain.VerificationCode.CodeType;
import com.app.ventas_api.seguridad.DTO.*;
import com.app.ventas_api.seguridad.IRepository.IRoleRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.IRepository.IRefreshTokenRepository;
import com.app.ventas_api.seguridad.IRepository.IVerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

/**
 * Authentication Service
 */
@Service
public class AuthService {
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private IRoleRepository roleRepository;
    
    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TotpService totpService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private IVerificationCodeRepository verificationCodeRepository;
    
    // Generador de código numérico
    private String generateNumericCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
    /**
     * Register new user
     */
    public AuthResponseDto register(RegisterRequestDto request) {
        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Get company entity if companyId is provided
        Company company = null;
        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.getCompanyId()));
        }
        
        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .company(company)
                .active(true)
                .build();
        
        Optional<Role> defaultRole = roleRepository.findByName("USER");
        if (defaultRole.isPresent()) {
            user.setRoles(new HashSet<>());
            user.getRoles().add(defaultRole.get());
        }
        
        user = userRepository.save(user);
        
        // Generate tokens
        String token = jwtService.generateToken(user.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getId());
        
        // Save refresh token
        saveRefreshToken(user.getId(), refreshToken);
        
        return AuthResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Registration successful")
                .build();
    }
    
    /**
     * Register new user with company (combined endpoint)
     */
    @Transactional
    public RegisterWithCompanyResponseDto registerWithCompany(RegisterWithCompanyRequestDto request) {
        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Check if company NIT exists - if not, create it
        Company company = companyRepository.findByNit(request.getCompany().getNit())
                .orElseGet(() -> {
                    // Create new company
                    Company newCompany = Company.builder()
                            .nit(request.getCompany().getNit())
                            .businessName(request.getCompany().getBusinessName())
                            .email(request.getCompany().getEmail())
                            .phone(request.getCompany().getPhone())
                            .address(request.getCompany().getAddress())
                            .logoUrl(request.getCompany().getLogoUrl())
                            .active(true)
                            .build();
                    return companyRepository.save(newCompany);
                });
        
        // Create new user linked to company (using entity relationship)
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .company(company)  // Using entity instead of companyId
                .active(true)
                .build();
        
        // Assign admin role
        Optional<Role> adminRole = roleRepository.findByName("ADMIN");
        if (adminRole.isPresent()) {
            user.setRoles(new HashSet<>());
            user.getRoles().add(adminRole.get());
        }
        
        user = userRepository.save(user);
        
        // Generate tokens
        String token = jwtService.generateToken(user.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getId());
        
        // Save refresh token
        saveRefreshToken(user.getId(), refreshToken);
        
        return RegisterWithCompanyResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Registration successful")
                .companyId(company.getId())
                .companyName(company.getBusinessName())
                .companyNit(company.getNit())
                .build();
    }
    
    /**
     * Login - Soporta 2FA por correo electrónico
     */
    @Transactional
    public AuthResponseDto login(LoginRequestDto request) {
        // Find user by username or email
        Optional<User> userOpt = userRepository.findByUsername(request.getUsernameOrEmail());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(request.getUsernameOrEmail());
        }
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }
        
        User user = userOpt.get();
        
        // Check if user is active
        if (!user.getActive()) {
            throw new RuntimeException("User account is disabled");
        }
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        // Check if 2FA is enabled - if so, require 2FA code via email
        if (user.getTwoFactorEnabled() != null && user.getTwoFactorEnabled()) {
            // Check if 2FA code was provided
            String twoFactorCode = request.getTwoFactorCode();
            if (twoFactorCode == null || twoFactorCode.isEmpty()) {
                // Generate and send 2FA code via email
                String code = generateNumericCode();
                saveVerificationCode(user, code, CodeType.LOGIN_2FA, 5);
                emailService.sendTwoFactorCode(user.getEmail(), user.getUsername(), code);
                
                // Return response indicating 2FA is required
                return AuthResponseDto.builder()
                        .token(null)
                        .refreshToken(null)
                        .userId(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .message("2FA_REQUIRED")
                        .twoFactorRequired(true)
                        .build();
            }
            
            // Validate 2FA code
            if (!verifyCode(user.getId(), twoFactorCode, CodeType.LOGIN_2FA)) {
                throw new RuntimeException("Código de verificación inválido o expirado");
            }
        }
        
        // Generate tokens
        String token = jwtService.generateToken(user.getUsername(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getId());
        
        // Save refresh token
        saveRefreshToken(user.getId(), refreshToken);
        
        // Update last login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        
        return AuthResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Login successful")
                .build();
    }
    
    /**
     * Forgot Password - Generar código numérico y enviar por email
     */
    @Transactional
    public String forgotPassword(ForgotPasswordRequestDto request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists
            return "Si el correo existe, se ha enviado un código de verificación";
        }
        
        User user = userOpt.get();
        
        // Generate 6-digit numeric code
        String code = generateNumericCode();
        
        // Save verification code (expires in 10 minutes)
        saveVerificationCode(user, code, CodeType.PASSWORD_RESET, 10);
        
        // Send email with code
        emailService.sendPasswordResetCode(user.getEmail(), user.getUsername(), code);
        
        return "Se ha enviado un código de verificación a tu correo electrónico";
    }
    
    /**
     * Reset Password with verification code
     */
    public String resetPassword(ResetPasswordDto request) {
        // Find user by email from the request
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        User user = userOpt.get();
        
        // Verify the code
        if (!verifyCode(user.getId(), request.getCode(), CodeType.PASSWORD_RESET)) {
            throw new RuntimeException("Código inválido o expirado");
        }
        
        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        
        userRepository.save(user);
        
        // Mark code as used
        markCodeAsUsed(user.getId(), request.getCode(), CodeType.PASSWORD_RESET);
        
        return "Contraseña restablecida exitosamente";
    }
    
    /**
     * Change Password (authenticated user)
     */
    public String changePassword(Long userId, ChangePasswordDto request) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        
        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        
        userRepository.save(user);
        
        return "Password changed successfully";
    }
    
    /**
     * Refresh Token
     */
    public AuthResponseDto refreshToken(String refreshToken) {
        // Validate refresh token
        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        String username = jwtService.extractUsername(refreshToken);
        Long userId = jwtService.extractUserId(refreshToken);
        
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        
        // Generate new tokens
        String newToken = jwtService.generateToken(user.getUsername(), user.getId());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getId());
        
        // Save new refresh token
        saveRefreshToken(user.getId(), newRefreshToken);
        
        return AuthResponseDto.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .message("Token refreshed successfully")
                .build();
    }
    
    /**
     * Save refresh token to database
     */
    private void saveRefreshToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        
        refreshTokenRepository.save(refreshToken);
    }
    
    /**
     * Save verification code to database
     */
    @Transactional
    private void saveVerificationCode(User user, String code, CodeType codeType, int minutesValid) {
        // Invalidate old codes of the same type
        verificationCodeRepository.invalidateOldCodes(user.getId(), codeType);
        
        // Create new verification code
        VerificationCode verificationCode = VerificationCode.builder()
                .user(user)
                .code(code)
                .codeType(codeType)
                .expiresAt(LocalDateTime.now().plusMinutes(minutesValid))
                .used(false)
                .build();
        
        verificationCodeRepository.save(verificationCode);
    }
    
    /**
     * Verify if a code is valid
     */
    private boolean verifyCode(Long userId, String code, CodeType codeType) {
        Optional<VerificationCode> codeOpt = verificationCodeRepository.findByCodeAndType(code, codeType);
        
        if (codeOpt.isEmpty()) {
            return false;
        }
        
        VerificationCode verificationCode = codeOpt.get();
        
        // Check if the code belongs to the correct user
        if (!verificationCode.getUser().getId().equals(userId)) {
            return false;
        }
        
        return verificationCode.isValid();
    }
    
    /**
     * Mark a code as used
     */
    private void markCodeAsUsed(Long userId, String code, CodeType codeType) {
        verificationCodeRepository.findByCodeAndType(code, codeType)
                .ifPresent(vc -> {
                    vc.setUsed(true);
                    verificationCodeRepository.save(vc);
                });
    }
}

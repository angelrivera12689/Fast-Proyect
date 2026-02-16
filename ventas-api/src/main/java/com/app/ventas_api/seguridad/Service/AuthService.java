package com.app.ventas_api.seguridad.Service;

import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.domain.RefreshToken;
import com.app.ventas_api.seguridad.domain.Role;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.DTO.*;
import com.app.ventas_api.seguridad.IRepository.IRoleRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.IRepository.IRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

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
     * Login
     */
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
     * Forgot Password - Generate reset token
     */
    public String forgotPassword(ForgotPasswordRequestDto request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists
            return "If the email exists, a reset link has been sent";
        }
        
        User user = userOpt.get();
        
        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpires(LocalDateTime.now().plusHours(24));
        
        userRepository.save(user);
        
        // In production, send email with reset link
        // For now, return the token (for testing)
        return "Password reset token: " + resetToken;
    }
    
    /**
     * Reset Password with token
     */
    public String resetPassword(ResetPasswordDto request) {
        Optional<User> userOpt = userRepository.findByPasswordResetToken(request.getToken());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired token");
        }
        
        User user = userOpt.get();
        
        // Check if token is expired
        if (user.getPasswordResetExpires() == null || 
            user.getPasswordResetExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }
        
        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        
        userRepository.save(user);
        
        return "Password reset successful";
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
}

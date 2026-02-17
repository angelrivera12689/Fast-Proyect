package com.app.ventas_api.seguridad.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.app.ventas_api.seguridad.DTO.Request.UserRequestDto;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IService.IUserService;

/**
 * SEGURIDAD - Controller
 * UserController
 * 
 * Roles: Solo ADMIN
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/users")
public class UserController {
    
    @Autowired
    private IUserService userService;
    
    // ===== Todos los métodos requieren rol ADMIN =====
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> findAll() {
        try {
            List<User> users = userService.all();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> findActive() {
        try {
            List<User> users = userService.findByStateTrue();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findById(id);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.findByUsername(username);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        try {
            Optional<User> user = userService.findByEmail(email);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> findByCompany(@PathVariable Long companyId) {
        try {
            List<User> users = userService.findByCompanyId(companyId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@Valid @RequestBody UserRequestDto request) {
        try {
            User user = User.builder()
                    .company(request.getCompanyId() != null ? 
                        com.app.ventas_api.Organizacion.Entity.Company.builder().id(request.getCompanyId()).build() : null)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .passwordHash(request.getPasswordHash())
                    .phone(request.getPhone())
                    .avatarUrl(request.getAvatarUrl())
                    .twoFactorEnabled(request.getTwoFactorEnabled() != null ? request.getTwoFactorEnabled() : false)
                    .twoFactorSecret(request.getTwoFactorSecret())
                    .passwordResetToken(request.getPasswordResetToken())
                    .passwordResetExpires(request.getPasswordResetExpires())
                    .active(request.getActive() != null ? request.getActive() : true)
                    .build();
            
            User saved = userService.save(user);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto request) {
        try {
            User user = User.builder()
                    .company(request.getCompanyId() != null ? 
                        com.app.ventas_api.Organizacion.Entity.Company.builder().id(request.getCompanyId()).build() : null)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .avatarUrl(request.getAvatarUrl())
                    .twoFactorEnabled(request.getTwoFactorEnabled())
                    .twoFactorSecret(request.getTwoFactorSecret())
                    .passwordResetToken(request.getPasswordResetToken())
                    .passwordResetExpires(request.getPasswordResetExpires())
                    .active(request.getActive())
                    .build();
            
            userService.update(id, user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

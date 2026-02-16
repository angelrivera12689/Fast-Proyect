package com.app.ventas_api.seguridad.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.ventas_api.seguridad.DTO.Request.UserRequestDto;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IService.IUserService;

/**
 * SEGURIDAD - Controller
 * UserController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/users")
public class UserController {
    
    @Autowired
    private IUserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        try {
            List<User> users = userService.all();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<User>> findActive() {
        try {
            List<User> users = userService.findByStateTrue();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
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
    public ResponseEntity<List<User>> findByCompany(@PathVariable Long companyId) {
        try {
            List<User> users = userService.findByCompanyId(companyId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserRequestDto request) {
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
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequestDto request) {
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

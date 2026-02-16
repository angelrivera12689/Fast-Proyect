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

import com.app.ventas_api.seguridad.DTO.Request.RefreshTokenRequestDto;
import com.app.ventas_api.seguridad.domain.RefreshToken;
import com.app.ventas_api.seguridad.IService.IRefreshTokenService;

/**
 * SEGURIDAD - Controller
 * RefreshTokenController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/refresh-tokens")
public class RefreshTokenController {
    
    @Autowired
    private IRefreshTokenService refreshTokenService;
    
    @GetMapping
    public ResponseEntity<List<RefreshToken>> findAll() {
        try {
            List<RefreshToken> tokens = refreshTokenService.all();
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RefreshToken> findById(@PathVariable Long id) {
        try {
            Optional<RefreshToken> token = refreshTokenService.findById(id);
            return token.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/token/{token}")
    public ResponseEntity<RefreshToken> findByToken(@PathVariable String token) {
        try {
            Optional<RefreshToken> tokenOpt = refreshTokenService.findByToken(token);
            return tokenOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RefreshToken>> findByUser(@PathVariable Long userId) {
        try {
            List<RefreshToken> tokens = refreshTokenService.findByUserId(userId);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<RefreshToken> create(@RequestBody RefreshTokenRequestDto request) {
        try {
            RefreshToken token = RefreshToken.builder()
                    .user(request.getUserId() != null ? 
                        com.app.ventas_api.seguridad.domain.User.builder().id(request.getUserId()).build() : null)
                    .token(request.getToken())
                    .expiresAt(request.getExpiresAt())
                    .revoked(request.getRevoked() != null ? request.getRevoked() : false)
                    .build();
            
            RefreshToken saved = refreshTokenService.save(token);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RefreshToken> update(@PathVariable Long id, @RequestBody RefreshTokenRequestDto request) {
        try {
            RefreshToken token = RefreshToken.builder()
                    .user(request.getUserId() != null ? 
                        com.app.ventas_api.seguridad.domain.User.builder().id(request.getUserId()).build() : null)
                    .token(request.getToken())
                    .expiresAt(request.getExpiresAt())
                    .revoked(request.getRevoked())
                    .build();
            
            refreshTokenService.update(id, token);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            refreshTokenService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/revoke/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Long id) {
        try {
            refreshTokenService.revokeToken(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

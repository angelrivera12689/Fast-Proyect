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

import com.app.ventas_api.seguridad.DTO.Request.RoleRequestDto;
import com.app.ventas_api.seguridad.domain.Role;
import com.app.ventas_api.seguridad.IService.IRoleService;

/**
 * SEGURIDAD - Controller
 * RoleController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/roles")
public class RoleController {
    
    @Autowired
    private IRoleService roleService;
    
    @GetMapping
    public ResponseEntity<List<Role>> findAll() {
        try {
            List<Role> roles = roleService.all();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) {
        try {
            Optional<Role> role = roleService.findById(id);
            return role.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Role> findByName(@PathVariable String name) {
        try {
            Optional<Role> role = roleService.findByName(name);
            return role.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Role> create(@RequestBody RoleRequestDto request) {
        try {
            Role role = Role.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .build();
            
            Role saved = roleService.save(role);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody RoleRequestDto request) {
        try {
            Role role = Role.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .build();
            
            roleService.update(id, role);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            roleService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

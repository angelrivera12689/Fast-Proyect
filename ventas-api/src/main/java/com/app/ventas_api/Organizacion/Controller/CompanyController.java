package com.app.ventas_api.Organizacion.Controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.ventas_api.Organizacion.DTO.Request.CompanyRequestDto;
import com.app.ventas_api.Organizacion.DTO.Response.CompanyResponseDto;
import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IService.ICompanyService;

/**
 * ORGANIZACION - Controller
 * CompanyController
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/companies")
public class CompanyController {
    
    @Autowired
    private ICompanyService companyService;
    
    @GetMapping
    public ResponseEntity<List<Company>> findAll() {
        try {
            List<Company> companies = companyService.all();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Company>> findActive() {
        try {
            List<Company> companies = companyService.findByStateTrue();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Company> findById(@PathVariable Long id) {
        try {
            Optional<Company> company = companyService.findById(id);
            return company.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/nit/{nit}")
    public ResponseEntity<Company> findByNit(@PathVariable String nit) {
        try {
            Optional<Company> company = companyService.findByNit(nit);
            return company.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<Company> findByEmail(@PathVariable String email) {
        try {
            Optional<Company> company = companyService.findByEmail(email);
            return company.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Company> create(@RequestBody CompanyRequestDto request) {
        try {
            Company company = Company.builder()
                    .nit(request.getNit())
                    .businessName(request.getBusinessName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .logoUrl(request.getLogoUrl())
                    .active(request.getActive() != null ? request.getActive() : true)
                    .build();
            
            Company saved = companyService.save(company);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody CompanyRequestDto request) {
        try {
            Company company = Company.builder()
                    .nit(request.getNit())
                    .businessName(request.getBusinessName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .logoUrl(request.getLogoUrl())
                    .active(request.getActive())
                    .build();
            
            companyService.update(id, company);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            companyService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

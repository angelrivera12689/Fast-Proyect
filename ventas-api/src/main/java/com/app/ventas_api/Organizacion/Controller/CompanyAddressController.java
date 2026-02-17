package com.app.ventas_api.Organizacion.Controller;

import com.app.ventas_api.Organizacion.DTO.Request.CompanyAddressRequestDto;
import com.app.ventas_api.Organizacion.DTO.Response.CompanyAddressResponseDto;
import com.app.ventas_api.Organizacion.Entity.CompanyAddress;
import com.app.ventas_api.Organizacion.IService.ICompanyAddressService;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IService.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies/{companyId}/addresses")
public class CompanyAddressController {

    private final ICompanyAddressService addressService;
    private final IUserService userService;

    public CompanyAddressController(ICompanyAddressService addressService, IUserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    // Get all addresses for a company
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<List<CompanyAddressResponseDto>> getCompanyAddresses(@PathVariable Long companyId) {
        List<CompanyAddress> addresses = addressService.getCompanyAddresses(companyId);
        List<CompanyAddressResponseDto> response = addresses.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Get specific address
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<CompanyAddressResponseDto> getAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId) {
        CompanyAddress address = addressService.getAddressById(companyId, addressId);
        return ResponseEntity.ok(toResponseDto(address));
    }

    // Create new address
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<CompanyAddressResponseDto> createAddress(
            @PathVariable Long companyId,
            @Valid @RequestBody CompanyAddressRequestDto request) {
        CompanyAddress address = addressService.createAddress(companyId, request);
        return ResponseEntity.ok(toResponseDto(address));
    }

    // Update address
    @PutMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<CompanyAddressResponseDto> updateAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId,
            @Valid @RequestBody CompanyAddressRequestDto request) {
        CompanyAddress address = addressService.updateAddress(companyId, addressId, request);
        return ResponseEntity.ok(toResponseDto(address));
    }

    // Delete address
    @DeleteMapping("/{addressId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId) {
        addressService.deleteAddress(companyId, addressId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Address deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Set as default address
    @PutMapping("/{addressId}/default")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    public ResponseEntity<CompanyAddressResponseDto> setDefaultAddress(
            @PathVariable Long companyId,
            @PathVariable Long addressId) {
        CompanyAddress address = addressService.setDefaultAddress(companyId, addressId);
        return ResponseEntity.ok(toResponseDto(address));
    }

    // Helper method to convert entity to DTO
    private CompanyAddressResponseDto toResponseDto(CompanyAddress address) {
        CompanyAddressResponseDto dto = new CompanyAddressResponseDto();
        dto.setId(address.getId());
        dto.setCompanyId(address.getCompany().getId());
        dto.setLabel(address.getLabel());
        dto.setAddress(address.getAddress());
        dto.setCity(address.getCity());
        dto.setDepartment(address.getDepartment());
        dto.setZipCode(address.getZipCode());
        dto.setContactName(address.getContactName());
        dto.setContactPhone(address.getContactPhone());
        dto.setIsDefault(address.getIsDefault());
        dto.setActive(address.getActive());
        dto.setCreatedAt(address.getCreatedAt());
        dto.setUpdatedAt(address.getUpdatedAt());
        return dto;
    }
}

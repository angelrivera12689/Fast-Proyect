package com.app.ventas_api.Organizacion.Service;

import com.app.ventas_api.Organizacion.DTO.Request.CompanyAddressRequestDto;
import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.Entity.CompanyAddress;
import com.app.ventas_api.Organizacion.IRepository.ICompanyAddressRepository;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.Organizacion.IService.ICompanyAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyAddressService implements ICompanyAddressService {

    private final ICompanyAddressRepository addressRepository;
    private final ICompanyRepository companyRepository;

    public CompanyAddressService(ICompanyAddressRepository addressRepository, ICompanyRepository companyRepository) {
        this.addressRepository = addressRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public CompanyAddress createAddress(Long companyId, CompanyAddressRequestDto request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // If this is set as default, clear other defaults
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultAddresses(companyId);
        }

        CompanyAddress address = new CompanyAddress();
        address.setCompany(company);
        address.setLabel(request.getLabel());
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setDepartment(request.getDepartment());
        address.setZipCode(request.getZipCode());
        address.setContactName(request.getContactName());
        address.setContactPhone(request.getContactPhone());
        address.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        address.setActive(true);

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public CompanyAddress updateAddress(Long companyId, Long addressId, CompanyAddressRequestDto request) {
        CompanyAddress address = addressRepository.findByIdAndCompanyId(addressId, companyId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // If this is set as default, clear other defaults
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.clearDefaultAddresses(companyId);
        }

        address.setLabel(request.getLabel());
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setDepartment(request.getDepartment());
        address.setZipCode(request.getZipCode());
        address.setContactName(request.getContactName());
        address.setContactPhone(request.getContactPhone());
        
        if (request.getIsDefault() != null) {
            address.setIsDefault(request.getIsDefault());
        }

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long companyId, Long addressId) {
        CompanyAddress address = addressRepository.findByIdAndCompanyId(addressId, companyId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        addressRepository.delete(address);
    }

    @Override
    public List<CompanyAddress> getCompanyAddresses(Long companyId) {
        return addressRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @Override
    public CompanyAddress getAddressById(Long companyId, Long addressId) {
        return addressRepository.findByIdAndCompanyId(addressId, companyId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Override
    @Transactional
    public CompanyAddress setDefaultAddress(Long companyId, Long addressId) {
        // Clear all defaults first
        addressRepository.clearDefaultAddresses(companyId);
        
        // Set the new default
        CompanyAddress address = addressRepository.findByIdAndCompanyId(addressId, companyId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        
        address.setIsDefault(true);
        return addressRepository.save(address);
    }
}

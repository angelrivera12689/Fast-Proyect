package com.app.ventas_api.Organizacion.IService;

import com.app.ventas_api.Organizacion.DTO.Request.CompanyAddressRequestDto;
import com.app.ventas_api.Organizacion.Entity.CompanyAddress;

import java.util.List;

public interface ICompanyAddressService {

    CompanyAddress createAddress(Long companyId, CompanyAddressRequestDto request);

    CompanyAddress updateAddress(Long companyId, Long addressId, CompanyAddressRequestDto request);

    void deleteAddress(Long companyId, Long addressId);

    List<CompanyAddress> getCompanyAddresses(Long companyId);

    CompanyAddress getAddressById(Long companyId, Long addressId);

    CompanyAddress setDefaultAddress(Long companyId, Long addressId);
}

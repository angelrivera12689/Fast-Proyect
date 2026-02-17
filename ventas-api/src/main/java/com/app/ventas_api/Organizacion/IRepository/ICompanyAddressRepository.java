package com.app.ventas_api.Organizacion.IRepository;

import com.app.ventas_api.Organizacion.Entity.CompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICompanyAddressRepository extends JpaRepository<CompanyAddress, Long> {

    List<CompanyAddress> findByCompanyId(Long companyId);

    List<CompanyAddress> findByCompanyIdAndActiveTrue(Long companyId);

    Optional<CompanyAddress> findByIdAndCompanyId(Long id, Long companyId);

    Optional<CompanyAddress> findByCompanyIdAndIsDefaultTrue(Long companyId);

    @Modifying
    @Query("UPDATE CompanyAddress a SET a.isDefault = false WHERE a.company.id = :companyId")
    void clearDefaultAddresses(Long companyId);

    boolean existsByCompanyIdAndIsDefaultTrue(Long companyId);
}

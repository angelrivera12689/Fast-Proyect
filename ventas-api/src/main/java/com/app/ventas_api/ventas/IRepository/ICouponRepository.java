package com.app.ventas_api.ventas.IRepository;

import com.app.ventas_api.ventas.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    List<Coupon> findByActiveTrue();

    List<Coupon> findByCompanyId(Long companyId);

    boolean existsByCode(String code);
}

package com.app.ventas_api.ventas.IService;

import com.app.ventas_api.ventas.domain.Coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ICouponService {

    Coupon createCoupon(Coupon coupon);

    Coupon updateCoupon(Long id, Coupon coupon);

    void deleteCoupon(Long id);

    List<Coupon> getAllCoupons();

    List<Coupon> getActiveCoupons();

    Coupon getCouponByCode(String code);

    Map<String, Object> validateCoupon(String code, BigDecimal purchaseAmount);

    Coupon useCoupon(String code);
}

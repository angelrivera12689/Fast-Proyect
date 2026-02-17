package com.app.ventas_api.ventas.Service;

import com.app.ventas_api.ventas.IService.ICouponService;
import com.app.ventas_api.ventas.domain.Coupon;
import com.app.ventas_api.ventas.IRepository.ICouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponService implements ICouponService {

    private final ICouponRepository couponRepository;

    public CouponService(ICouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    @Transactional
    public Coupon createCoupon(Coupon coupon) {
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new RuntimeException("El código del cupón ya existe");
        }
        coupon.setActive(true);
        return couponRepository.save(coupon);
    }

    @Override
    @Transactional
    public Coupon updateCoupon(Long id, Coupon coupon) {
        Coupon existing = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        
        existing.setDescription(coupon.getDescription());
        existing.setType(coupon.getType());
        existing.setValue(coupon.getValue());
        existing.setMinPurchaseAmount(coupon.getMinPurchaseAmount());
        existing.setMaxUses(coupon.getMaxUses());
        existing.setValidFrom(coupon.getValidFrom());
        existing.setValidUntil(coupon.getValidUntil());
        existing.setActive(coupon.getActive());
        
        return couponRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public List<Coupon> getActiveCoupons() {
        return couponRepository.findByActiveTrue();
    }

    @Override
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code.toUpperCase())
                .orElse(null);
    }

    @Override
    public Map<String, Object> validateCoupon(String code, BigDecimal purchaseAmount) {
        Map<String, Object> result = new HashMap<>();
        
        Coupon coupon = getCouponByCode(code);
        
        if (coupon == null) {
            result.put("valid", false);
            result.put("message", "Cupón no encontrado");
            return result;
        }
        
        if (!coupon.isValid()) {
            result.put("valid", false);
            result.put("message", "Cupón expirado o inactivo");
            return result;
        }
        
        if (coupon.getMinPurchaseAmount() != null && 
            purchaseAmount.compareTo(coupon.getMinPurchaseAmount()) < 0) {
            result.put("valid", false);
            result.put("message", "Monto mínimo de compra: $" + coupon.getMinPurchaseAmount());
            return result;
        }
        
        BigDecimal discount = coupon.calculateDiscount(purchaseAmount);
        
        result.put("valid", true);
        result.put("message", "Cupón aplicado");
        result.put("discount", discount);
        result.put("coupon", coupon);
        
        return result;
    }

    @Override
    @Transactional
    public Coupon useCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        
        if (!coupon.isValid()) {
            throw new RuntimeException("Cupón no válido");
        }
        
        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        return couponRepository.save(coupon);
    }
}

package com.app.ventas_api.ventas.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;  // CODIGO10, BIENVENIDO20, etc.

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;  // PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING

    @Column(nullable = false)
    private BigDecimal value;  // 10 = 10%, 20000 = $20.000

    @Column(name = "min_purchase_amount")
    private BigDecimal minPurchaseAmount;  // Compra mínima para aplicar

    @Column(name = "max_uses")
    private Integer maxUses;  // Veces que puede usarse (null = ilimitado)

    @Column(name = "current_uses")
    private Integer currentUses;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private com.app.ventas_api.Organizacion.Entity.Company company;  // null = para todas las empresas

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.currentUses == null) {
            this.currentUses = 0;
        }
    }

    public enum CouponType {
        PERCENTAGE,      // Porcentaje (10%)
        FIXED_AMOUNT,    // Monto fijo ($20.000)
        FREE_SHIPPING    // Envío gratis
    }

    public boolean isValid() {
        if (!active) return false;
        
        LocalDateTime now = LocalDateTime.now();
        if (validFrom != null && now.isBefore(validFrom)) return false;
        if (validUntil != null && now.isAfter(validUntil)) return false;
        if (maxUses != null && currentUses >= maxUses) return false;
        
        return true;
    }

    public BigDecimal calculateDiscount(BigDecimal purchaseAmount) {
        if (!isValid()) return BigDecimal.ZERO;
        if (minPurchaseAmount != null && purchaseAmount.compareTo(minPurchaseAmount) < 0) {
            return BigDecimal.ZERO;
        }

        switch (type) {
            case PERCENTAGE:
                return purchaseAmount.multiply(value).divide(BigDecimal.valueOf(100));
            case FIXED_AMOUNT:
                return value.min(purchaseAmount);  // No puede ser mayor que la compra
            case FREE_SHIPPING:
                return value;  // Devolver valor del envío
            default:
                return BigDecimal.ZERO;
        }
    }
}

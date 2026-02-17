package com.app.ventas_api.ventas.domain;

import com.app.ventas_api.seguridad.domain.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * VENTAS - Domain
 * Entidad: SalesReport
 * 
 * Reportes de ventas generados para el dashboard del admin
 */
@Entity
@Table(name = "sales_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "total_orders")
    private Integer totalOrders;
    
    @Column(name = "total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue;
    
    @Column(name = "total_products_sold")
    private Integer totalProductsSold;
    
    @Column(name = "average_order_value", precision = 15, scale = 2)
    private BigDecimal averageOrderValue;
    
    @Column(name = "total_companies")
    private Integer totalCompanies;
    
    @Column(name = "total_users")
    private Integer totalUsers;
    
    @Column(name = "total_products")
    private Integer totalProducts;
    
    @Column(name = "low_stock_products")
    private Integer lowStockProducts;
    
    @Column(name = "pending_quotes")
    private Integer pendingQuotes;
    
    @Column(name = "approved_quotes")
    private Integer approvedQuotes;
    
    @Column(name = "pending_payments")
    private Integer pendingPayments;
    
    @Column(name = "completed_payments")
    private Integer completedPayments;
    
    @Column(name = "top_products", columnDefinition = "JSON")
    private String topProducts;  // JSON con productos más vendidos
    
    @Column(name = "top_companies", columnDefinition = "JSON")
    private String topCompanies;  // JSON con empresas más activas
    
    @Column(name = "sales_by_day", columnDefinition = "JSON")
    private String salesByDay;  // JSON con ventas por día
    
    @Column(name = "sales_by_category", columnDefinition = "JSON")
    private String salesByCategory;  // JSON con ventas por categoría
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private User generatedBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum ReportType {
        DAILY,      // Reporte diario
        WEEKLY,     // Reporte semanal
        MONTHLY,    // Reporte mensual
        YEARLY,     // Reporte anual
        CUSTOM      // Reporte personalizado
    }
}

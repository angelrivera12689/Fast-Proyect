package com.app.ventas_api.ventas.DTO.Response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * VENTAS - DTO Response
 * DashboardMetricsDto - Métricas en tiempo real para el dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMetricsDto {
    
    // Métricas de ventas
    private Integer totalOrders;
    private BigDecimal totalRevenue;
    private Integer totalProductsSold;
    private BigDecimal averageOrderValue;
    
    // Métricas de usuarios y empresas
    private Integer totalCompanies;
    private Integer totalUsers;
    private Integer activeCompanies;
    
    // Métricas de productos
    private Integer totalProducts;
    private Integer lowStockProducts;
    private Integer outOfStockProducts;
    private Integer activeProducts;
    
    // Métricas de cotizaciones
    private Integer pendingQuotes;
    private Integer approvedQuotes;
    private Integer rejectedQuotes;
    
    // Métricas de pagos
    private Integer pendingPayments;
    private Integer completedPayments;
    private Integer failedPayments;
    
    // Productos más vendidos
    private List<ProductSalesDto> topProducts;
    
    // Empresas más activas
    private List<CompanySalesDto> topCompanies;
    
    // Ventas por día (últimos 30 días)
    private List<DailySalesDto> salesByDay;
    
    // Ventas por categoría
    private List<CategorySalesDto> salesByCategory;
    
    // Estado de pedidos
    private Map<String, Integer> ordersByStatus;
    
    // Tickets
    private BigDecimal highestOrderValue;
    private BigDecimal lowestOrderValue;
    
    // Tasas
    private BigDecimal conversionRate;
    private BigDecimal cancellationRate;
    
    /**
     * DTO para productos más vendidos
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSalesDto {
        private Long productId;
        private String productName;
        private String productSku;
        private Integer quantitySold;
        private BigDecimal totalRevenue;
    }
    
    /**
     * DTO para empresas más activas
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompanySalesDto {
        private Long companyId;
        private String companyName;
        private String companyNit;
        private Integer orderCount;
        private BigDecimal totalSpent;
    }
    
    /**
     * DTO para ventas diarias
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DailySalesDto {
        private String date;
        private Integer orderCount;
        private BigDecimal revenue;
        private Integer productsSold;
    }
    
    /**
     * DTO para ventas por categoría
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategorySalesDto {
        private Long categoryId;
        private String categoryName;
        private Integer productCount;
        private Integer quantitySold;
        private BigDecimal revenue;
    }
}

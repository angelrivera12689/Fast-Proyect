package com.app.ventas_api.ventas.Service;

import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.Productos.Entity.Category;
import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.ICategoryRepository;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.ventas.DTO.Request.DashboardRequestDto;
import com.app.ventas_api.ventas.DTO.Response.DashboardMetricsDto;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.ventas.IRepository.IPaymentRepository;
import com.app.ventas_api.ventas.IRepository.IQuoteRepository;
import com.app.ventas_api.ventas.IRepository.ISalesReportRepository;
import com.app.ventas_api.ventas.IService.IDashboardService;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.domain.Payment;
import com.app.ventas_api.ventas.domain.Quote;
import com.app.ventas_api.ventas.domain.SalesReport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * VENTAS - Service
 * Implementation: DashboardService
 */
@Service
public class DashboardService implements IDashboardService {
    
    @Autowired
    private IOrderRepository orderRepository;
    
    @Autowired
    private IPaymentRepository paymentRepository;
    
    @Autowired
    private IQuoteRepository quoteRepository;
    
    @Autowired
    private IProductRepository productRepository;
    
    @Autowired
    private ICategoryRepository categoryRepository;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Autowired
    private ISalesReportRepository salesReportRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsDto getDashboardMetrics() {
        return calculateMetrics(null, null, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsDto getDashboardMetricsByDateRange(LocalDate startDate, LocalDate endDate) {
        return calculateMetrics(startDate, endDate, null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsDto getDashboardMetricsByCompany(Long companyId) {
        return calculateMetrics(null, null, companyId);
    }
    
    private DashboardMetricsDto calculateMetrics(LocalDate startDate, LocalDate endDate, Long companyId) {
        DashboardMetricsDto metrics = new DashboardMetricsDto();
        
        // Determinar rango de fechas
        LocalDate effectiveStartDate = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate effectiveEndDate = endDate != null ? endDate : LocalDate.now();
        LocalDateTime startDateTime = effectiveStartDate.atStartOfDay();
        LocalDateTime endDateTime = effectiveEndDate.atTime(23, 59, 59);
        
        // Query base para pedidos
        List<Order> orders;
        List<Payment> payments;
        
        if (companyId != null) {
            orders = orderRepository.findByCompany_Id(companyId).stream()
                    .filter(o -> o.getCreatedAt() != null && 
                                !o.getCreatedAt().isBefore(startDateTime) && 
                                !o.getCreatedAt().isAfter(endDateTime))
                    .collect(Collectors.toList());
            payments = paymentRepository.findAll().stream()
                    .filter(p -> p.getOrder() != null && 
                                p.getOrder().getCompany() != null &&
                                p.getOrder().getCompany().getId().equals(companyId))
                    .collect(Collectors.toList());
        } else {
            orders = orderRepository.findAll().stream()
                    .filter(o -> o.getCreatedAt() != null && 
                                !o.getCreatedAt().isBefore(startDateTime) && 
                                !o.getCreatedAt().isAfter(endDateTime))
                    .collect(Collectors.toList());
            payments = paymentRepository.findAll().stream()
                    .filter(p -> p.getCreatedAt() != null &&
                                !p.getCreatedAt().isBefore(startDateTime) &&
                                !p.getCreatedAt().isAfter(endDateTime))
                    .collect(Collectors.toList());
        }
        
        // Métricas de pedidos
        metrics.setTotalOrders(orders.size());
        
        BigDecimal totalRevenue = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PAID || o.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        metrics.setTotalRevenue(totalRevenue);
        
        // Productos vendidos
        int totalProductsSold = orders.stream()
                .filter(o -> o.getItems() != null)
                .mapToInt(o -> o.getItems().stream()
                        .mapToInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                        .sum())
                .sum();
        metrics.setTotalProductsSold(totalProductsSold);
        
        // Valor promedio por pedido
        long completedOrdersCount = orders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PAID || o.getStatus() == Order.OrderStatus.DELIVERED)
                .count();
        if (completedOrdersCount > 0) {
            BigDecimal avgOrderValue = totalRevenue.divide(
                    BigDecimal.valueOf(completedOrdersCount), 
                    2, RoundingMode.HALF_UP);
            metrics.setAverageOrderValue(avgOrderValue);
        } else {
            metrics.setAverageOrderValue(BigDecimal.ZERO);
        }
        
        // Métricas de empresas
        List<Company> companies = companyRepository.findAll();
        metrics.setTotalCompanies(companies.size());
        metrics.setActiveCompanies((int) companies.stream()
                .filter(c -> c.getActive() != null && c.getActive())
                .count());
        
        // Métricas de usuarios
        List<User> users = userRepository.findAll();
        metrics.setTotalUsers(users.size());
        
        // Métricas de productos
        List<Product> products = productRepository.findAll();
        metrics.setTotalProducts(products.size());
        metrics.setActiveProducts((int) products.stream()
                .filter(p -> p.getActive() != null && p.getActive())
                .count());
        metrics.setLowStockProducts((int) products.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0 && p.getStock() <= 10)
                .count());
        metrics.setOutOfStockProducts((int) products.stream()
                .filter(p -> p.getStock() == null || p.getStock() == 0)
                .count());
        
        // Métricas de cotizaciones
        List<Quote> quotes = quoteRepository.findAll();
        metrics.setPendingQuotes((int) quotes.stream()
                .filter(q -> q.getStatus() == Quote.QuoteStatus.PENDING)
                .count());
        metrics.setApprovedQuotes((int) quotes.stream()
                .filter(q -> q.getStatus() == Quote.QuoteStatus.APPROVED)
                .count());
        metrics.setRejectedQuotes((int) quotes.stream()
                .filter(q -> q.getStatus() == Quote.QuoteStatus.REJECTED)
                .count());
        
        // Métricas de pagos
        metrics.setPendingPayments((int) payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.PENDING)
                .count());
        metrics.setCompletedPayments((int) payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.PAID)
                .count());
        metrics.setFailedPayments((int) payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.FAILED)
                .count());
        
        // Pedidos por estado
        Map<String, Integer> ordersByStatus = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getStatus() != null ? o.getStatus().name() : "UNKNOWN",
                        Collectors.summingInt(o -> 1)
                ));
        metrics.setOrdersByStatus(ordersByStatus);
        
        // Productos más vendidos
        Map<Long, Integer> productSales = new HashMap<>();
        for (Order order : orders) {
            if (order.getItems() != null) {
                for (var item : order.getItems()) {
                    if (item.getProduct() != null && item.getProduct().getId() != null) {
                        Long productId = item.getProduct().getId();
                        int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                        productSales.merge(productId, qty, Integer::sum);
                    }
                }
            }
        }
        
        List<DashboardMetricsDto.ProductSalesDto> topProducts = productSales.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey()).orElse(null);
                    if (product == null) return null;
                    BigDecimal revenue = product.getBasePrice() != null 
                            ? product.getBasePrice().multiply(BigDecimal.valueOf(entry.getValue()))
                            : BigDecimal.ZERO;
                    return DashboardMetricsDto.ProductSalesDto.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .productSku(product.getSku())
                            .quantitySold(entry.getValue())
                            .totalRevenue(revenue)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        metrics.setTopProducts(topProducts);
        
        // Empresas más activas
        Map<Long, BigDecimal> companySales = new HashMap<>();
        Map<Long, Integer> companyOrders = new HashMap<>();
        for (Order order : orders) {
            if (order.getCompany() != null && order.getCompany().getId() != null) {
                Long companyId2 = order.getCompany().getId();
                BigDecimal amount = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
                companySales.merge(companyId2, amount, BigDecimal::add);
                companyOrders.merge(companyId2, 1, Integer::sum);
            }
        }
        
        List<DashboardMetricsDto.CompanySalesDto> topCompanies = companySales.entrySet().stream()
                .sorted(Map.Entry.<Long, BigDecimal>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Company company = companyRepository.findById(entry.getKey()).orElse(null);
                    if (company == null) return null;
                    return DashboardMetricsDto.CompanySalesDto.builder()
                            .companyId(company.getId())
                            .companyName(company.getBusinessName())
                            .companyNit(company.getNit())
                            .orderCount(companyOrders.get(entry.getKey()))
                            .totalSpent(entry.getValue())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        metrics.setTopCompanies(topCompanies);
        
        // Ventas por día
        Map<String, Integer> dailyOrders = new HashMap<>();
        Map<String, BigDecimal> dailyRevenue = new HashMap<>();
        for (Order order : orders) {
            if (order.getCreatedAt() != null) {
                String dateKey = order.getCreatedAt().toLocalDate().toString();
                dailyOrders.merge(dateKey, 1, Integer::sum);
                BigDecimal amount = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
                dailyRevenue.merge(dateKey, amount, BigDecimal::add);
            }
        }
        
        List<DashboardMetricsDto.DailySalesDto> salesByDay = dailyOrders.entrySet().stream()
                .map(entry -> DashboardMetricsDto.DailySalesDto.builder()
                        .date(entry.getKey())
                        .orderCount(entry.getValue())
                        .revenue(dailyRevenue.get(entry.getKey()))
                        .build())
                .sorted(Comparator.comparing(DashboardMetricsDto.DailySalesDto::getDate))
                .collect(Collectors.toList());
        metrics.setSalesByDay(salesByDay);
        
        // Ventas por categoría
        Map<Long, Integer> categorySales = new HashMap<>();
        for (Order order : orders) {
            if (order.getItems() != null) {
                for (var item : order.getItems()) {
                    if (item.getProduct() != null && item.getProduct().getCategory() != null) {
                        Long categoryId = item.getProduct().getCategory().getId();
                        int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                        categorySales.merge(categoryId, qty, Integer::sum);
                    }
                }
            }
        }
        
        List<DashboardMetricsDto.CategorySalesDto> salesByCategory = categorySales.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .map(entry -> {
                    Category category = categoryRepository.findById(entry.getKey()).orElse(null);
                    if (category == null) return null;
                    return DashboardMetricsDto.CategorySalesDto.builder()
                            .categoryId(category.getId())
                            .categoryName(category.getName())
                            .quantitySold(entry.getValue())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        metrics.setSalesByCategory(salesByCategory);
        
        // Tickets más altos y bajos
        orders.stream()
                .filter(o -> o.getTotalAmount() != null)
                .map(Order::getTotalAmount)
                .max(BigDecimal::compareTo)
                .ifPresent(metrics::setHighestOrderValue);
        
        orders.stream()
                .filter(o -> o.getTotalAmount() != null && o.getTotalAmount().compareTo(BigDecimal.ZERO) > 0)
                .map(Order::getTotalAmount)
                .min(BigDecimal::compareTo)
                .ifPresent(metrics::setLowestOrderValue);
        
        // Tasas de conversión y cancelación
        if (!orders.isEmpty()) {
            long completedOrders2 = orders.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.PAID || o.getStatus() == Order.OrderStatus.DELIVERED)
                    .count();
            BigDecimal conversionRate = BigDecimal.valueOf(completedOrders2)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);
            metrics.setConversionRate(conversionRate);
            
            long cancelledOrders = orders.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.CANCELLED)
                    .count();
            BigDecimal cancellationRate = BigDecimal.valueOf(cancelledOrders)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP);
            metrics.setCancellationRate(cancellationRate);
        }
        
        return metrics;
    }
    
    @Override
    @Transactional
    public SalesReport generateReport(DashboardRequestDto request, Long userId) {
        LocalDate startDate;
        LocalDate endDate;
        SalesReport.ReportType reportType;
        
        if (request.getType() == DashboardRequestDto.ReportType.CUSTOM) {
            startDate = request.getStartDate();
            endDate = request.getEndDate();
            reportType = SalesReport.ReportType.CUSTOM;
        } else if (request.getType() == DashboardRequestDto.ReportType.DAILY) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
            reportType = SalesReport.ReportType.DAILY;
        } else if (request.getType() == DashboardRequestDto.ReportType.WEEKLY) {
            startDate = LocalDate.now().minusWeeks(1);
            endDate = LocalDate.now();
            reportType = SalesReport.ReportType.WEEKLY;
        } else if (request.getType() == DashboardRequestDto.ReportType.MONTHLY) {
            startDate = LocalDate.now().minusMonths(1);
            endDate = LocalDate.now();
            reportType = SalesReport.ReportType.MONTHLY;
        } else if (request.getType() == DashboardRequestDto.ReportType.YEARLY) {
            startDate = LocalDate.now().minusYears(1);
            endDate = LocalDate.now();
            reportType = SalesReport.ReportType.YEARLY;
        } else {
            startDate = LocalDate.of(2020, 1, 1);
            endDate = LocalDate.now();
            reportType = SalesReport.ReportType.YEARLY;
        }
        
        DashboardMetricsDto metrics = calculateMetrics(startDate, endDate, request.getCompanyId());
        
        User user = userRepository.findById(userId).orElse(null);
        
        SalesReport report = SalesReport.builder()
                .reportType(reportType)
                .startDate(startDate)
                .endDate(endDate)
                .totalOrders(metrics.getTotalOrders())
                .totalRevenue(metrics.getTotalRevenue())
                .totalProductsSold(metrics.getTotalProductsSold())
                .averageOrderValue(metrics.getAverageOrderValue())
                .totalCompanies(metrics.getTotalCompanies())
                .totalUsers(metrics.getTotalUsers())
                .totalProducts(metrics.getTotalProducts())
                .lowStockProducts(metrics.getLowStockProducts())
                .pendingQuotes(metrics.getPendingQuotes())
                .approvedQuotes(metrics.getApprovedQuotes())
                .pendingPayments(metrics.getPendingPayments())
                .completedPayments(metrics.getCompletedPayments())
                .generatedBy(user)
                .build();
        
        // Convertir listas a JSON
        try {
            if (metrics.getTopProducts() != null) {
                report.setTopProducts(objectMapper.writeValueAsString(metrics.getTopProducts()));
            }
            if (metrics.getTopCompanies() != null) {
                report.setTopCompanies(objectMapper.writeValueAsString(metrics.getTopCompanies()));
            }
            if (metrics.getSalesByDay() != null) {
                report.setSalesByDay(objectMapper.writeValueAsString(metrics.getSalesByDay()));
            }
            if (metrics.getSalesByCategory() != null) {
                report.setSalesByCategory(objectMapper.writeValueAsString(metrics.getSalesByCategory()));
            }
        } catch (Exception e) {
            // Ignorar errores de serialización
        }
        
        return salesReportRepository.save(report);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SalesReport> getAllReports() {
        return salesReportRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public SalesReport getReportById(Long id) {
        return salesReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }
    
    @Override
    @Transactional
    public void deleteReport(Long id) {
        salesReportRepository.deleteById(id);
    }
}

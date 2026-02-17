package com.app.ventas_api.ventas.Controller;

import com.app.ventas_api.seguridad.Service.JwtService;
import com.app.ventas_api.ventas.DTO.Request.DashboardRequestDto;
import com.app.ventas_api.ventas.DTO.Response.DashboardMetricsDto;
import com.app.ventas_api.ventas.IService.IDashboardService;
import com.app.ventas_api.ventas.domain.SalesReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * VENTAS - Controller
 * DashboardController - Endpoints para dashboard y reportes
 */
@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {
    
    @Autowired
    private IDashboardService dashboardService;
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * GET /api/dashboard/metrics - Métricas en tiempo real del dashboard
     */
    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDto> getDashboardMetrics() {
        try {
            DashboardMetricsDto metrics = dashboardService.getDashboardMetrics();
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/dashboard/metrics/date-range?startDate=2024-01-01&endDate=2024-12-31
     * Métricas por rango de fechas
     */
    @GetMapping("/metrics/date-range")
    public ResponseEntity<DashboardMetricsDto> getMetricsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            DashboardMetricsDto metrics = dashboardService.getDashboardMetricsByDateRange(
                    java.time.LocalDate.parse(startDate),
                    java.time.LocalDate.parse(endDate)
            );
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/dashboard/metrics/company/{companyId}
     * Métricas por empresa específica
     */
    @GetMapping("/metrics/company/{companyId}")
    public ResponseEntity<DashboardMetricsDto> getMetricsByCompany(@PathVariable Long companyId) {
        try {
            DashboardMetricsDto metrics = dashboardService.getDashboardMetricsByCompany(companyId);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * POST /api/dashboard/reports - Generar un nuevo reporte
     */
    @PostMapping("/reports")
    public ResponseEntity<SalesReport> generateReport(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody DashboardRequestDto request) {
        try {
            Long userId = extractUserIdFromToken(authHeader);
            SalesReport report = dashboardService.generateReport(request, userId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/dashboard/reports - Obtener todos los reportes
     */
    @GetMapping("/reports")
    public ResponseEntity<List<SalesReport>> getAllReports() {
        try {
            List<SalesReport> reports = dashboardService.getAllReports();
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/dashboard/reports/{id} - Obtener un reporte específico
     */
    @GetMapping("/reports/{id}")
    public ResponseEntity<SalesReport> getReportById(@PathVariable Long id) {
        try {
            SalesReport report = dashboardService.getReportById(id);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/dashboard/reports/{id} - Eliminar un reporte
     */
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        try {
            dashboardService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/dashboard/health - Verificar que el endpoint funciona
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Dashboard API is running");
    }
    
    /**
     * Helper method to extract user ID from token
     */
    private Long extractUserIdFromToken(String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtService.validateToken(token)) {
                    return jwtService.extractUserId(token);
                }
            }
            return 1L; // Default user ID for testing
        } catch (Exception e) {
            return 1L;
        }
    }
}

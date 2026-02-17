package com.app.ventas_api.ventas.IService;

import com.app.ventas_api.ventas.DTO.Request.DashboardRequestDto;
import com.app.ventas_api.ventas.DTO.Response.DashboardMetricsDto;
import com.app.ventas_api.ventas.domain.SalesReport;

import java.util.List;

/**
 * VENTAS - IService
 * Interface: IDashboardService
 */
public interface IDashboardService {
    
    /**
     * Obtiene métricas en tiempo real del dashboard
     */
    DashboardMetricsDto getDashboardMetrics();
    
    /**
     * Obtiene métricas por rango de fechas
     */
    DashboardMetricsDto getDashboardMetricsByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
    
    /**
     * Obtiene métricas por empresa específica
     */
    DashboardMetricsDto getDashboardMetricsByCompany(Long companyId);
    
    /**
     * Genera y guarda un reporte
     */
    SalesReport generateReport(DashboardRequestDto request, Long userId);
    
    /**
     * Obtiene todos los reportes generados
     */
    List<SalesReport> getAllReports();
    
    /**
     * Obtiene un reporte por ID
     */
    SalesReport getReportById(Long id);
    
    /**
     * Elimina un reporte
     */
    void deleteReport(Long id);
}

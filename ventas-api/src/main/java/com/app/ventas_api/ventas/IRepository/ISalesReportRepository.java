package com.app.ventas_api.ventas.IRepository;

import com.app.ventas_api.ventas.domain.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * VENTAS - IRepository
 * Interface: ISalesReportRepository
 */
@Repository
public interface ISalesReportRepository extends JpaRepository<SalesReport, Long> {
    
    List<SalesReport> findByReportType(SalesReport.ReportType reportType);
    
    List<SalesReport> findByGeneratedBy_Id(Long userId);
    
    @Query("SELECT sr FROM SalesReport sr WHERE sr.createdAt BETWEEN :startDate AND :endDate ORDER BY sr.createdAt DESC")
    List<SalesReport> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
}

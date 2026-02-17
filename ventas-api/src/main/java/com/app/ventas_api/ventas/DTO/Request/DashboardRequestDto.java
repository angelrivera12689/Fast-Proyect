package com.app.ventas_api.ventas.DTO.Request;

import lombok.*;
import java.time.LocalDate;

/**
 * VENTAS - DTO Request
 * DashboardRequestDto - Request para generar reportes personalizados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardRequestDto {
    
    private ReportType type;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private Long companyId;
    
    public enum ReportType {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY,
        CUSTOM,
        ALL_TIME
    }
}

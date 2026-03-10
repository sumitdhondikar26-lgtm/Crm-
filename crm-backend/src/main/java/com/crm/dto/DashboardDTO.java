package com.crm.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private long totalCustomers;
    private long activeCustomers;
    private long totalDeals;
    private BigDecimal totalDealValue;
    private BigDecimal wonDealValue;
    private long openTasks;
    private long overdueTasks;
    private long activitiesThisWeek;

    private Map<String, Long> customersByStatus;
    private Map<String, Long> dealsByStage;
    private Map<String, BigDecimal> dealValueByStage;
    private Map<String, Long> tasksByStatus;
    private Map<String, Long> activitiesByType;
}

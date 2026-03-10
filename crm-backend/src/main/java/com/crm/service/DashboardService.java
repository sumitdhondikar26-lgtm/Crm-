package com.crm.service;

import com.crm.dto.DashboardDTO;
import com.crm.enums.CustomerStatus;
import com.crm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public DashboardDTO getDashboardMetrics() {
        // Customer metrics
        long totalCustomers = customerRepository.count();
        long activeCustomers = customerRepository.countByStatus(CustomerStatus.ACTIVE);

        // Deal metrics
        long totalDeals = dealRepository.count();
        BigDecimal totalDealValue = dealRepository.sumAllDealValues();
        BigDecimal wonDealValue = dealRepository.sumWonDealValues();

        // Task metrics
        long openTasks = taskRepository.countOpenTasks();
        long overdueTasks = taskRepository.countOverdueTasks(LocalDate.now());

        // Activity metrics — this week
        LocalDateTime startOfWeek = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        long activitiesThisWeek = activityRepository.countByActivityDateBetween(startOfWeek, endOfWeek);

        // Grouped data
        Map<String, Long> customersByStatus = new LinkedHashMap<>();
        customerRepository.countByStatusGrouped()
                .forEach(row -> customersByStatus.put(row[0].toString(), (Long) row[1]));

        Map<String, Long> dealsByStage = new LinkedHashMap<>();
        dealRepository.countByStageGrouped().forEach(row -> dealsByStage.put(row[0].toString(), (Long) row[1]));

        Map<String, BigDecimal> dealValueByStage = new LinkedHashMap<>();
        dealRepository.sumValueByStageGrouped()
                .forEach(row -> dealValueByStage.put(row[0].toString(), (BigDecimal) row[1]));

        Map<String, Long> tasksByStatus = new LinkedHashMap<>();
        taskRepository.countByStatusGrouped().forEach(row -> tasksByStatus.put(row[0].toString(), (Long) row[1]));

        Map<String, Long> activitiesByType = new LinkedHashMap<>();
        activityRepository.countByTypeGrouped().forEach(row -> activitiesByType.put(row[0].toString(), (Long) row[1]));

        return DashboardDTO.builder()
                .totalCustomers(totalCustomers)
                .activeCustomers(activeCustomers)
                .totalDeals(totalDeals)
                .totalDealValue(totalDealValue)
                .wonDealValue(wonDealValue)
                .openTasks(openTasks)
                .overdueTasks(overdueTasks)
                .activitiesThisWeek(activitiesThisWeek)
                .customersByStatus(customersByStatus)
                .dealsByStage(dealsByStage)
                .dealValueByStage(dealValueByStage)
                .tasksByStatus(tasksByStatus)
                .activitiesByType(activitiesByType)
                .build();
    }
}

package com.crm.controller;

import com.crm.dto.DashboardDTO;
import com.crm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }
}

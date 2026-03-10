package com.crm.controller;

import com.crm.dto.DealDTO;
import com.crm.service.DealService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deals")
public class DealController {

    @Autowired
    private DealService dealService;

    @GetMapping
    public ResponseEntity<Page<DealDTO>> getAllDeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(dealService.getAllDeals(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealDTO> getDealById(@PathVariable Long id) {
        return ResponseEntity.ok(dealService.getDealById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<DealDTO>> getDealsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(dealService.getDealsByCustomer(customerId, pageable));
    }

    @GetMapping("/stage/{stage}")
    public ResponseEntity<Page<DealDTO>> getDealsByStage(
            @PathVariable String stage,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(dealService.getDealsByStage(stage, pageable));
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<Page<DealDTO>> getDealsByAssignee(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(dealService.getDealsByAssignee(userId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DealDTO>> searchDeals(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(dealService.searchDeals(q, pageable));
    }

    @PostMapping
    public ResponseEntity<DealDTO> createDeal(@Valid @RequestBody DealDTO dealDTO) {
        return new ResponseEntity<>(dealService.createDeal(dealDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DealDTO> updateDeal(
            @PathVariable Long id,
            @Valid @RequestBody DealDTO dealDTO) {
        return ResponseEntity.ok(dealService.updateDeal(id, dealDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.noContent().build();
    }
}

package com.crm.controller;

import com.crm.dto.ActivityDTO;
import com.crm.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<Page<ActivityDTO>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "activityDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(activityService.getAllActivities(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ActivityDTO>> getActivitiesByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(activityService.getActivitiesByCustomer(customerId, pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<ActivityDTO>> getActivitiesByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(activityService.getActivitiesByType(type, pageable));
    }

    @PostMapping
    public ResponseEntity<ActivityDTO> createActivity(@Valid @RequestBody ActivityDTO activityDTO) {
        return new ResponseEntity<>(activityService.createActivity(activityDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDTO> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.updateActivity(id, activityDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}

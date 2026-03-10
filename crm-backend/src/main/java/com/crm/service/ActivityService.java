package com.crm.service;

import com.crm.dto.ActivityDTO;
import com.crm.entity.Activity;
import com.crm.entity.Customer;
import com.crm.entity.User;
import com.crm.enums.ActivityType;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.ActivityRepository;
import com.crm.repository.CustomerRepository;
import com.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<ActivityDTO> getAllActivities(Pageable pageable) {
        return activityRepository.findAll(pageable).map(this::toDTO);
    }

    public ActivityDTO getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
        return toDTO(activity);
    }

    public Page<ActivityDTO> getActivitiesByCustomer(Long customerId, Pageable pageable) {
        return activityRepository.findByCustomerId(customerId, pageable).map(this::toDTO);
    }

    public Page<ActivityDTO> getActivitiesByType(String type, Pageable pageable) {
        ActivityType activityType = ActivityType.valueOf(type.toUpperCase());
        return activityRepository.findByType(activityType, pageable).map(this::toDTO);
    }

    public ActivityDTO createActivity(ActivityDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId()));

        Activity activity = toEntity(dto);
        activity.setCustomer(customer);

        if (dto.getPerformedById() != null) {
            User user = userRepository.findById(dto.getPerformedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getPerformedById()));
            activity.setPerformedBy(user);
        }

        activity = activityRepository.save(activity);
        return toDTO(activity);
    }

    public ActivityDTO updateActivity(Long id, ActivityDTO dto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));

        activity.setType(ActivityType.valueOf(dto.getType().toUpperCase()));
        activity.setSubject(dto.getSubject());
        activity.setDescription(dto.getDescription());
        activity.setActivityDate(LocalDateTime.parse(dto.getActivityDate()));
        activity.setDurationMinutes(dto.getDurationMinutes());
        activity.setOutcome(dto.getOutcome());

        activity = activityRepository.save(activity);
        return toDTO(activity);
    }

    public void deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
        activityRepository.delete(activity);
    }

    private ActivityDTO toDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .type(activity.getType().name())
                .subject(activity.getSubject())
                .description(activity.getDescription())
                .activityDate(activity.getActivityDate().toString())
                .durationMinutes(activity.getDurationMinutes())
                .outcome(activity.getOutcome())
                .customerId(activity.getCustomer().getId())
                .customerName(activity.getCustomer().getFullName())
                .performedById(activity.getPerformedBy() != null ? activity.getPerformedBy().getId() : null)
                .performedByName(activity.getPerformedBy() != null ? activity.getPerformedBy().getFullName() : null)
                .createdAt(activity.getCreatedAt() != null ? activity.getCreatedAt().toString() : null)
                .build();
    }

    private Activity toEntity(ActivityDTO dto) {
        return Activity.builder()
                .type(ActivityType.valueOf(dto.getType().toUpperCase()))
                .subject(dto.getSubject())
                .description(dto.getDescription())
                .activityDate(LocalDateTime.parse(dto.getActivityDate()))
                .durationMinutes(dto.getDurationMinutes())
                .outcome(dto.getOutcome())
                .build();
    }
}

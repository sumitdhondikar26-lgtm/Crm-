package com.crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDTO {

    private Long id;

    @NotBlank(message = "Activity type is required")
    private String type;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String description;

    @NotBlank(message = "Activity date is required")
    private String activityDate;

    private Integer durationMinutes;
    private String outcome;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private String customerName;

    private Long performedById;
    private String performedByName;
    private String createdAt;
}

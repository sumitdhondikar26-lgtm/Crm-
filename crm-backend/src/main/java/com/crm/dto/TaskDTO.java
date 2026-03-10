package com.crm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String status;
    private String priority;
    private String dueDate;
    private String completedDate;
    private Long customerId;
    private String customerName;
    private Long assignedToId;
    private String assignedToName;
    private String createdAt;
    private String updatedAt;
}

package com.crm.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Deal value is required")
    @DecimalMin(value = "0.0", message = "Value must be positive")
    private BigDecimal value;

    private String stage;

    @Min(value = 0, message = "Probability must be between 0 and 100")
    @Max(value = 100, message = "Probability must be between 0 and 100")
    private Integer probability;

    private String expectedCloseDate;
    private String actualCloseDate;
    private String source;
    private String notes;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private String customerName;

    private Long assignedToId;
    private String assignedToName;
    private String createdAt;
    private String updatedAt;
}

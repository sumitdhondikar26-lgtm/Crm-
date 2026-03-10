package com.crm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String email;
    private String phone;
    private String mobilePhone;
    private String jobTitle;
    private String department;
    private boolean primary;
    private String notes;
    private Long customerId;
    private String customerName;
    private String createdAt;
    private String updatedAt;
}

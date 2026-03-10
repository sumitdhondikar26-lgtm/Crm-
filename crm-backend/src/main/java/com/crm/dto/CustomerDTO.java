package com.crm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String company;
    private String jobTitle;
    private String industry;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String status;
    private String source;
    private String notes;
    private Long assignedToId;
    private String assignedToName;
    private int contactCount;
    private int dealCount;
    private String createdAt;
    private String updatedAt;
}

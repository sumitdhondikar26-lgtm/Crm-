package com.crm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "mobile_phone", length = 20)
    private String mobilePhone;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(length = 100)
    private String department;

    @Column(name = "is_primary")
    @Builder.Default
    private boolean primary = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

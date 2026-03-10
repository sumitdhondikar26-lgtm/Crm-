package com.crm.entity;

import com.crm.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String company;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(length = 100)
    private String industry;

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String state;

    @Column(length = 50)
    private String country;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    @Builder.Default
    private CustomerStatus status = CustomerStatus.LEAD;

    @Column(length = 100)
    private String source;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Deal> deals = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

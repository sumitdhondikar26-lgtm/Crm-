package com.crm.entity;

import com.crm.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Deal> deals = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Task> tasks = new HashSet<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

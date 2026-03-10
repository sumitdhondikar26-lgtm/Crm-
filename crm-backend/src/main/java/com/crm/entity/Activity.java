package com.crm.entity;

import com.crm.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    private ActivityType type;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "activity_date", nullable = false)
    private LocalDateTime activityDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(length = 200)
    private String outcome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;
}

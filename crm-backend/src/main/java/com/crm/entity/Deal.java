package com.crm.entity;

import com.crm.enums.DealStage;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "deal_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(50)")
    @Builder.Default
    private DealStage stage = DealStage.QUALIFICATION;

    @Column(nullable = false)
    @Builder.Default
    private Integer probability = 10;

    @Column(name = "expected_close_date")
    private LocalDate expectedCloseDate;

    @Column(name = "actual_close_date")
    private LocalDate actualCloseDate;

    @Column(length = 100)
    private String source;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
}

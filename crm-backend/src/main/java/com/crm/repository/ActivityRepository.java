package com.crm.repository;

import com.crm.entity.Activity;
import com.crm.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByCustomerId(Long customerId, Pageable pageable);

    Page<Activity> findByPerformedById(Long userId, Pageable pageable);

    Page<Activity> findByType(ActivityType type, Pageable pageable);

    long countByActivityDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT a.type, COUNT(a) FROM Activity a GROUP BY a.type")
    List<Object[]> countByTypeGrouped();

    @Query("SELECT a FROM Activity a WHERE a.activityDate BETWEEN :start AND :end")
    Page<Activity> findByDateRange(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable);
}

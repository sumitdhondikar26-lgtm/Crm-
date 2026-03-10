package com.crm.repository;

import com.crm.entity.Task;
import com.crm.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByCustomerId(Long customerId, Pageable pageable);

    Page<Task> findByAssignedToId(Long userId, Pageable pageable);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    long countByStatus(TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status != 'COMPLETED' AND t.status != 'CANCELLED' AND t.dueDate < :today")
    long countOverdueTasks(LocalDate today);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.status IN ('TODO', 'IN_PROGRESS')")
    long countOpenTasks();

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countByStatusGrouped();

    Page<Task> findByAssignedToIdAndStatus(Long userId, TaskStatus status, Pageable pageable);
}

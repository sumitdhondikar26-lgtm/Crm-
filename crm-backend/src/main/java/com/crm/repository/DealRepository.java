package com.crm.repository;

import com.crm.entity.Deal;
import com.crm.enums.DealStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    Page<Deal> findByCustomerId(Long customerId, Pageable pageable);

    Page<Deal> findByAssignedToId(Long userId, Pageable pageable);

    Page<Deal> findByStage(DealStage stage, Pageable pageable);

    long countByStage(DealStage stage);

    @Query("SELECT COALESCE(SUM(d.value), 0) FROM Deal d")
    BigDecimal sumAllDealValues();

    @Query("SELECT COALESCE(SUM(d.value), 0) FROM Deal d WHERE d.stage = 'CLOSED_WON'")
    BigDecimal sumWonDealValues();

    @Query("SELECT d.stage, COUNT(d) FROM Deal d GROUP BY d.stage")
    List<Object[]> countByStageGrouped();

    @Query("SELECT d.stage, COALESCE(SUM(d.value), 0) FROM Deal d GROUP BY d.stage")
    List<Object[]> sumValueByStageGrouped();

    @Query("SELECT d FROM Deal d WHERE " +
            "LOWER(d.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Deal> searchDeals(String search, Pageable pageable);
}

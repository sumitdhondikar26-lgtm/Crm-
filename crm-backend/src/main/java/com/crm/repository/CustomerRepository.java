package com.crm.repository;

import com.crm.entity.Customer;
import com.crm.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);

    Page<Customer> findByAssignedToId(Long userId, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.company) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Customer> searchCustomers(@Param("search") String search, Pageable pageable);

    long countByStatus(CustomerStatus status);

    List<Customer> findByCompanyIgnoreCase(String company);

    boolean existsByEmail(String email);

    @Query("SELECT c.status, COUNT(c) FROM Customer c GROUP BY c.status")
    List<Object[]> countByStatusGrouped();
}

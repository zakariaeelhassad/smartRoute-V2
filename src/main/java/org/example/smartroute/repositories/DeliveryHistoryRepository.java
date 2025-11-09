package org.example.smartroute.repositories;

import org.example.smartroute.entities.models.DeliveryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory , Long> {

    Page<DeliveryHistory> findByDayOfWeek(DayOfWeek dayOfWeek, Pageable pageable);

    @Query("SELECT h FROM DeliveryHistory h WHERE LOWER(h.customer.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<DeliveryHistory> findByCustomerNameContaining(String name, Pageable pageable);
}

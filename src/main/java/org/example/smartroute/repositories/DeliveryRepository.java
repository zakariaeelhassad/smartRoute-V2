package org.example.smartroute.repositories;

import org.example.smartroute.entities.models.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findByAddressContainingIgnoreCase(String address, Pageable pageable);

    Page<Delivery> findByDeliveryStatus(String status, Pageable pageable);

    @Query("""
        SELECT d FROM Delivery d
        WHERE d.latitude BETWEEN :minLat AND :maxLat
          AND d.longitude BETWEEN :minLon AND :maxLon
          AND d.timeWindow BETWEEN :startTime AND :endTime
    """)
    Page<Delivery> searchByAreaAndTimeWindow(double minLat, double maxLat,
                                             double minLon, double maxLon,
                                             String startTime, String endTime,
                                             Pageable pageable);
}

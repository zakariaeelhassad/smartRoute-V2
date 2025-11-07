package org.example.smartroute.repositories;

import org.example.smartroute.entities.models.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory , Long> {
}

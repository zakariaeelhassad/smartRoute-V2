package org.example.smartroute.repositories;

import org.example.smartroute.entities.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse , Long> {
}

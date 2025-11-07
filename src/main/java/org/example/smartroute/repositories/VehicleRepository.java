package org.example.smartroute.repositories;

import org.example.smartroute.entities.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle , Long> {
}

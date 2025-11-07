package org.example.smartroute.entities.DTO.tour;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.smartroute.entities.enums.AlgorithmType;
import org.example.smartroute.entities.enums.TourStatus;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Vehicle;
import org.example.smartroute.entities.models.Warehouse;

import java.time.LocalDate;
import java.util.List;

public record UpdateTourDto(
        LocalDate date ,
        Double totalDistance,
        Warehouse warehouse,
        Vehicle vehicle,
        List<Delivery> deliveries ,
        TourStatus status
) {
}

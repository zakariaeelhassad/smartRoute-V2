package org.example.smartroute.entities.DTO.tour;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.smartroute.entities.enums.AlgorithmType;
import org.example.smartroute.entities.enums.TourStatus;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Vehicle;
import org.example.smartroute.entities.models.Warehouse;

import java.time.LocalDate;
import java.util.List;

public record TourDto(
        @Positive Long id,
        @NotNull LocalDate date,
        @Positive Double totalDistance,
        @NotNull Warehouse warehouse,
        @NotNull Vehicle vehicle,
        @NotNull List<Delivery> deliveries ,
        @NotNull TourStatus status
) {
}

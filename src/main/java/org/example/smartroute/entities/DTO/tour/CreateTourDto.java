package org.example.smartroute.entities.DTO.tour;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreateTourDto(
        @NotNull LocalDate date,
        @NotNull Long warehouseId,
        @NotNull Long vehicleId,
        List<Long> deliveryIds
) {
}
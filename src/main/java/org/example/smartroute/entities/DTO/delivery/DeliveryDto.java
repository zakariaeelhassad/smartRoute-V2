package org.example.smartroute.entities.DTO.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.smartroute.entities.enums.DeliveryStatus;
import org.example.smartroute.entities.models.Tour;

import java.time.LocalDateTime;

public record DeliveryDto(
        @NotNull Long id,
        @NotBlank String address,
        @NotNull @Positive Double latitude,
        @NotNull @Positive Double longitude,
        @NotNull @Positive Double weight,
        @NotNull @Positive Double volume,
        @NotBlank String timeWindow,
        LocalDateTime plannedTime,
        LocalDateTime actualTime,
        @NotNull DeliveryStatus deliveryStatus,
        @NotNull Tour tour
) {
}

package org.example.smartroute.entities.DTO.delivery;

import org.example.smartroute.entities.enums.DeliveryStatus;
import org.example.smartroute.entities.models.Tour;

import java.time.LocalDateTime;

public record UpdateDeliveryDto(
        String address,
        Double latitude,
        Double longitude,
        Double weight,
        Double volume,
        String timeWindow,
        LocalDateTime plannedTime,
        LocalDateTime actualTime,
        DeliveryStatus deliveryStatus,
        Tour tour
) {
}

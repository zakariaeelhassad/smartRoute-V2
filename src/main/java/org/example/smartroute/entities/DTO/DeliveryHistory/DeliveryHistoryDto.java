package org.example.smartroute.entities.DTO.DeliveryHistory;

import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class DeliveryHistoryDto {
    private Long id;
    private Long deliveryId;
    private Long customerId;
    private Long tourId;
    private LocalDateTime plannedTime;
    private LocalDateTime actualTime;
    private Long delay;
    private DayOfWeek dayOfWeek;
}

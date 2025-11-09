package org.example.smartroute.entities.DTO.deliveryHistory;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class CreateDeliveryHistoryDto {

    @NotNull private Long deliveryId;
    @NotNull private Long customerId;
    @NotNull private Long tourId;
    @NotNull private LocalDateTime plannedTime;
    private LocalDateTime actualTime;
    private Long delay;
    @NotNull private DayOfWeek dayOfWeek;
}

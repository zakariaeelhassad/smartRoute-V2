package org.example.smartroute.entities.DTO.DeliveryHistory;

import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class UpdateDeliveryHistoryDto {

    private LocalDateTime plannedTime;
    private LocalDateTime actualTime;
    private Long delay;
    private DayOfWeek dayOfWeek;
    private Long tourId;
}

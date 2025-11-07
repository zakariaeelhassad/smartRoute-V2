package org.example.smartroute.listeners;

import org.example.smartroute.entities.DTO.DeliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.events.TourCompletedEvent;
import org.example.smartroute.services.IDeliveryHistoryService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TourCompletionListener {

    private final IDeliveryHistoryService deliveryHistoryService;

    public TourCompletionListener(IDeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    @EventListener
    public void handleTourCompleted(TourCompletedEvent event) {
        Tour tour = event.getTour();

        if (tour.getDeliveries() == null || tour.getDeliveries().isEmpty()) return;

        for (Delivery delivery : tour.getDeliveries()) {
            CreateDeliveryHistoryDto dto = new CreateDeliveryHistoryDto();
            dto.setDeliveryId(delivery.getId());
            dto.setCustomerId(delivery.getCustomer().getId());
            dto.setTourId(tour.getId());
            dto.setPlannedTime(delivery.getPlannedTime());
            dto.setActualTime(delivery.getActualTime());
            dto.setDelay(calculateDelay(delivery));
            dto.setDayOfWeek(tour.getDate().getDayOfWeek());

            deliveryHistoryService.create(dto);
        }
    }

    private Long calculateDelay(Delivery delivery) {
        if (delivery.getPlannedTime() == null || delivery.getActualTime() == null) return 0L;
        return java.time.Duration.between(delivery.getPlannedTime(), delivery.getActualTime()).toMinutes();
    }

}

package org.example.smartroute.events;

import org.example.smartroute.entities.models.Tour;
import org.springframework.context.ApplicationEvent;

public class TourCompletedEvent extends ApplicationEvent {
    private final Tour tour;

    public TourCompletedEvent(Object source, Tour tour) {
        super(source);
        this.tour = tour;
    }

    public Tour getTour() {
        return tour;
    }
}

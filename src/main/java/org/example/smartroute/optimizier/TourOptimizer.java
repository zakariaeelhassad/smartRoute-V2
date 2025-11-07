package org.example.smartroute.optimizier;

import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;

import java.util.List;

public interface TourOptimizer {
    List<Delivery> optimizerTour(Tour tour);
}

package org.example.smartroute.optimizier.impl;

import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.entities.models.Warehouse;
import org.example.smartroute.optimizier.TourOptimizer;
import org.example.smartroute.utils.DistanceCalculator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@ConditionalOnProperty(name = "optimizer.type", havingValue = "nearest")
public class NearestNeighborOptimizer implements TourOptimizer {

    private final DistanceCalculator distanceCalculator;

    public NearestNeighborOptimizer(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = Objects.requireNonNull(distanceCalculator, "distanceCalculator must not be null");
    }

    @Override
    public List<Delivery> optimizerTour(Tour tour) {
        if (tour == null) return List.of();
        Warehouse warehouse = tour.getWarehouse();
        List<Delivery> originals = tour.getDeliveries();

        if (originals == null || originals.isEmpty()) return List.of();
        if (warehouse == null) {
            return new ArrayList<>(originals);
        }

        List<Delivery> remaining = new ArrayList<>(originals);
        List<Delivery> ordered = new ArrayList<>();

        double currentLat = warehouse.getLatitude();
        double currentLon = warehouse.getLongitude();

        while (!remaining.isEmpty()) {
            Delivery nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (Delivery d : remaining) {
                if (d == null || d.getLatitude() == null || d.getLongitude() == null) continue;

                double dist = distanceCalculator.distance(currentLat, currentLon, d.getLatitude(), d.getLongitude());
                if (dist < minDistance) {
                    minDistance = dist;
                    nearest = d;
                }
            }

            if (nearest == null) break;

            ordered.add(nearest);
            remaining.remove(nearest);

            currentLat = nearest.getLatitude();
            currentLon = nearest.getLongitude();
        }

        return ordered;
    }
}

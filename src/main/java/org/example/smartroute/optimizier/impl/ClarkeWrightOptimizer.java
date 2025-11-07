package org.example.smartroute.optimizier.impl;

import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.entities.models.Warehouse;
import org.example.smartroute.optimizier.TourOptimizer;
import org.example.smartroute.utils.DistanceCalculator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ConditionalOnProperty(name = "optimizer.type", havingValue = "clarke")
public class ClarkeWrightOptimizer implements TourOptimizer {

    private final DistanceCalculator distanceCalculator;

    public ClarkeWrightOptimizer(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public List<Delivery> optimizerTour(Tour tour) {
        Warehouse warehouse = tour.getWarehouse();
        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());

        if (warehouse == null || deliveries == null || deliveries.size() <= 1) {
            return deliveries != null ? deliveries : Collections.emptyList();
        }

        List<Saving> savingsList = new ArrayList<>();
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery di = deliveries.get(i);
                Delivery dj = deliveries.get(j);

                double dWi = distanceCalculator.distance(warehouse, di);
                double dWj = distanceCalculator.distance(warehouse, dj);
                double dij = distanceCalculator.distance(di, dj);

                double saving = dWi + dWj - dij;
                savingsList.add(new Saving(di, dj, saving));
            }
        }

        savingsList.sort((a, b) -> Double.compare(b.value, a.value));

        Map<Delivery, Route> deliveryToRoute = new HashMap<>();
        List<Route> routes = new ArrayList<>();

        for (Saving s : savingsList) {
            Route routeI = deliveryToRoute.get(s.i);
            Route routeJ = deliveryToRoute.get(s.j);

            if (routeI == null && routeJ == null) {
                Route newRoute = new Route();
                newRoute.add(s.i);
                newRoute.add(s.j);
                routes.add(newRoute);
                deliveryToRoute.put(s.i, newRoute);
                deliveryToRoute.put(s.j, newRoute);
            } else if (routeI != null && routeJ == null && routeI.canAppend(s.i)) {
                routeI.add(s.j);
                deliveryToRoute.put(s.j, routeI);
            } else if (routeJ != null && routeI == null && routeJ.canPrepend(s.j)) {
                routeJ.prepend(s.i);
                deliveryToRoute.put(s.i, routeJ);
            } else if (routeI != null && routeJ != null && routeI != routeJ) {
                if (routeI.canAppend(s.i) && routeJ.canPrepend(s.j)) {
                    routeI.merge(routeJ);
                    for (Delivery d : routeJ.deliveries) {
                        deliveryToRoute.put(d, routeI);
                    }
                    routes.remove(routeJ);
                }
            }
        }

        Route finalRoute = routes.stream()
                .max(Comparator.comparingInt(r -> r.deliveries.size()))
                .orElseGet(() -> {
                    Route r = new Route();
                    r.deliveries.addAll(deliveries);
                    return r;
                });

        return new ArrayList<>(finalRoute.deliveries);
    }

    private static class Route {
        List<Delivery> deliveries = new ArrayList<>();

        void add(Delivery d) {
            deliveries.add(d);
        }

        void prepend(Delivery d) {
            deliveries.add(0, d);
        }

        boolean canAppend(Delivery d) {
            return deliveries.get(deliveries.size() - 1).equals(d);
        }

        boolean canPrepend(Delivery d) {
            return deliveries.get(0).equals(d);
        }

        void merge(Route other) {
            this.deliveries.addAll(other.deliveries);
        }
    }

    private static class Saving {
        Delivery i;
        Delivery j;
        double value;

        Saving(Delivery i, Delivery j, double value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }
    }
}

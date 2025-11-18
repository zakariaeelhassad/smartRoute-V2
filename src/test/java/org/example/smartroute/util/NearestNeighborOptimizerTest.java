package org.example.smartroute.util;

import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.entities.models.Warehouse;
import org.example.smartroute.optimizier.impl.NearestNeighborOptimizer;
import org.example.smartroute.utils.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NearestNeighborOptimizerTest {

    private NearestNeighborOptimizer optimizer;
    private DistanceCalculator distanceCalculator;

    @BeforeEach
    void setUp() {
        distanceCalculator = new DistanceCalculator();
        optimizer = new NearestNeighborOptimizer(distanceCalculator);
    }

    @Test
    void testOptimizerTour_ReturnsEmptyList_WhenNoDeliveries() {
        Tour tour = new Tour();
        tour.setDeliveries(List.of());

        List<Delivery> result = optimizer.optimizerTour(tour);
        assertTrue(result.isEmpty(), "Result should be empty when no deliveries exist");
    }

    @Test
    void testOptimizerTour_ReturnsOriginalList_WhenNoWarehouse() {
        Delivery d1 = createDelivery(1L, 34.0, -6.0);
        Delivery d2 = createDelivery(2L, 35.0, -6.5);

        Tour tour = new Tour();
        tour.setDeliveries(List.of(d1, d2));
        tour.setWarehouse(null);

        List<Delivery> result = optimizer.optimizerTour(tour);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(d1, d2)));
    }

    @Test
    void testOptimizerTour_ReturnsOrderedList_ByNearestNeighbor() {
        Warehouse warehouse = new Warehouse();
        warehouse.setLatitude(34.0);
        warehouse.setLongitude(-6.0);

        Delivery d1 = createDelivery(1L, 34.1, -6.1); // قريب بزاف
        Delivery d2 = createDelivery(2L, 35.0, -7.0); // بعيد

        Tour tour = new Tour();
        tour.setWarehouse(warehouse);
        tour.setDeliveries(List.of(d1, d2));

        List<Delivery> result = optimizer.optimizerTour(tour);

        assertEquals(2, result.size());
        assertEquals(d1, result.get(0), "Nearest delivery should be first");
        assertEquals(d2, result.get(1), "Farthest delivery should be last");
    }

    private Delivery createDelivery(Long id, double lat, double lon) {
        Delivery d = new Delivery();
        d.setId(id);
        d.setLatitude(lat);
        d.setLongitude(lon);
        return d;
    }
}

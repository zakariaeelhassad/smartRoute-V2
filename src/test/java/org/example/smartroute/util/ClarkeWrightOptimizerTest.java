package org.example.smartroute.util;

import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.entities.models.Warehouse;
import org.example.smartroute.optimizier.impl.ClarkeWrightOptimizer;
import org.example.smartroute.utils.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClarkeWrightOptimizerTest {

    private DistanceCalculator distanceCalculator;
    private ClarkeWrightOptimizer optimizer;

    @BeforeEach
    void setUp() {
        distanceCalculator = new DistanceCalculator();
        optimizer = new ClarkeWrightOptimizer(distanceCalculator);
    }

    @Test
    void testOptimizerWithSimpleTour() {
        Warehouse warehouse = new Warehouse();
        warehouse.setLatitude(0.0);
        warehouse.setLongitude(0.0);

        Delivery d1 = new Delivery();
        d1.setId(1L);
        d1.setLatitude(0.0);
        d1.setLongitude(1.0);

        Delivery d2 = new Delivery();
        d2.setId(2L);
        d2.setLatitude(1.0);
        d2.setLongitude(1.0);

        Delivery d3 = new Delivery();
        d3.setId(3L);
        d3.setLatitude(1.0);
        d3.setLongitude(0.0);

        Tour tour = new Tour();
        tour.setWarehouse(warehouse);
        tour.setDeliveries(List.of(d1, d2, d3));

        List<Delivery> optimized = optimizer.optimizerTour(tour);

        assertNotNull(optimized);
        assertEquals(3, optimized.size());
        assertTrue(optimized.containsAll(List.of(d1, d2, d3)));
    }

    @Test
    void testOptimizerWithEmptyTour() {
        Warehouse warehouse = new Warehouse();
        warehouse.setLatitude(0.0);
        warehouse.setLongitude(0.0);

        Tour tour = new Tour();
        tour.setWarehouse(warehouse);
        tour.setDeliveries(List.of());

        List<Delivery> optimized = optimizer.optimizerTour(tour);

        assertTrue(optimized.isEmpty());
    }

    @Test
    void testOptimizerWithSingleDelivery() {
        Warehouse warehouse = new Warehouse();
        warehouse.setLatitude(0.0);
        warehouse.setLongitude(0.0);

        Delivery d1 = new Delivery();
        d1.setLatitude(1.0);
        d1.setLongitude(1.0);

        Tour tour = new Tour();
        tour.setWarehouse(warehouse);
        tour.setDeliveries(List.of(d1));

        List<Delivery> optimized = optimizer.optimizerTour(tour);

        assertEquals(1, optimized.size());
        assertEquals(d1, optimized.get(0));
    }

    @Test
    void testOptimizerWithNullWarehouse() {
        Delivery d1 = new Delivery();
        d1.setLatitude(1.0);
        d1.setLongitude(1.0);

        Delivery d2 = new Delivery();
        d2.setLatitude(2.0);
        d2.setLongitude(2.0);

        Tour tour = new Tour();
        tour.setWarehouse(null);
        tour.setDeliveries(List.of(d1, d2));

        List<Delivery> optimized = optimizer.optimizerTour(tour);

        assertEquals(2, optimized.size());
    }
}

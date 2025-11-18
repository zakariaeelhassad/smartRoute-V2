package org.example.smartroute.services.impl;

import org.example.smartroute.entities.DTO.tour.CreateTourDto;
import org.example.smartroute.entities.DTO.tour.TourDto;
import org.example.smartroute.entities.DTO.tour.UpdateTourDto;
import org.example.smartroute.entities.enums.TourStatus;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.entities.models.Vehicle;
import org.example.smartroute.entities.models.Warehouse;
import org.example.smartroute.events.TourCompletedEvent;
import org.example.smartroute.mappers.TourMapper;
import org.example.smartroute.optimizier.OptimizerFactory;
import org.example.smartroute.optimizier.TourOptimizer;
import org.example.smartroute.repositories.DeliveryRepository;
import org.example.smartroute.repositories.TourRepository;
import org.example.smartroute.repositories.VehicleRepository;
import org.example.smartroute.repositories.WarehouseRepository;
import org.example.smartroute.services.ITourService;
import org.example.smartroute.utils.DistanceCalculator;
import org.example.smartroute.utils.TourUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TourService implements ITourService {

    private final TourRepository tourRepository;
    private final DeliveryRepository deliveryRepository;
    private final TourMapper tourMapper;
    private final DistanceCalculator distanceCalculator;
    private final OptimizerFactory optimizerFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final WarehouseRepository warehouseRepository;
    private final VehicleRepository vehicleRepository;

    @Value("${optimizer.default}")
    private String optimizerType;

    public TourService(
            TourRepository tourRepository,
            DeliveryRepository deliveryRepository,
            TourMapper tourMapper,
            DistanceCalculator distanceCalculator,
            OptimizerFactory optimizerFactory,
            ApplicationEventPublisher eventPublisher,
            WarehouseRepository warehouseRepository,
            VehicleRepository vehicleRepository
    ) {
        this.tourRepository = tourRepository;
        this.deliveryRepository = deliveryRepository;
        this.tourMapper = tourMapper;
        this.distanceCalculator = distanceCalculator;
        this.optimizerFactory = optimizerFactory;
        this.eventPublisher = eventPublisher;
        this.warehouseRepository = warehouseRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public TourDto create(CreateTourDto dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.warehouseId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        Vehicle vehicle = vehicleRepository.findById(dto.vehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        Tour tour = new Tour();
        tour.setDate(dto.date());
        tour.setWarehouse(warehouse);
        tour.setVehicle(vehicle);
        if (dto.deliveryIds() != null && !dto.deliveryIds().isEmpty()) {
            List<Delivery> deliveries = deliveryRepository.findAllById(dto.deliveryIds());
            deliveries.forEach(d -> d.setTour(tour));
            tour.setDeliveries(deliveries);
            List<Delivery> optimized = optimizeList(tour);
            tour.setDeliveries(optimized);
        } else {
            tour.setTotalDistance(0.0);
        }
        Tour saved = tourRepository.save(tour);
        return tourMapper.toDto(saved);
    }

    @Override
    public TourDto update(Long id, UpdateTourDto dto) {
        Tour existing = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        existing.setDate(dto.date());
        existing.setTotalDistance(dto.totalDistance());
        existing.setWarehouse(dto.warehouse());
        existing.setVehicle(dto.vehicle());
        if (dto.deliveries() != null) {
            existing.getDeliveries().clear();
            existing.getDeliveries().addAll(dto.deliveries());
            existing.getDeliveries().forEach(d -> d.setTour(existing));
        }
        if (existing.getDeliveries() != null && !existing.getDeliveries().isEmpty()) {
            List<Delivery> optimized = optimizeList(existing);
            existing.setDeliveries(optimized);
        }
        if (dto.status() != null) {
            existing.setStatus(dto.status());
            if (dto.status() == TourStatus.COMPLETED) {
                eventPublisher.publishEvent(new TourCompletedEvent(this, existing));
            }
        }
        Tour updated = tourRepository.save(existing);
        return tourMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        tourRepository.deleteById(id);
    }

    @Override
    public TourDto getById(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        if (tour.getDeliveries() != null && !tour.getDeliveries().isEmpty()) {
            List<Delivery> optimized = optimizeList(tour);
            tour.setDeliveries(optimized);
        }
        return tourMapper.toDto(tour);
    }

    @Override
    public List<TourDto> getAll() {
        return tourRepository.findAll().stream()
                .peek(tour -> {
                    if (tour.getDeliveries() != null && !tour.getDeliveries().isEmpty()) {
                        List<Delivery> optimized = optimizeList(tour);
                        tour.setDeliveries(optimized);
                    }
                })
                .map(tourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void optimizeTour(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        if (tour.getDeliveries() == null || tour.getDeliveries().isEmpty()) {
            throw new RuntimeException("No deliveries to optimize for this tour");
        }
        List<Delivery> optimized = optimizeList(tour);
        tour.setDeliveries(optimized);
        tourRepository.save(tour);
    }

    @Override
    public TourDto addDeliveriesToTour(Long tourId, List<Long> deliveryIds) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
        List<Delivery> deliveries = deliveryRepository.findAllById(deliveryIds);
        deliveries.forEach(d -> d.setTour(tour));
        tour.getDeliveries().addAll(deliveries);
        if (!tour.getDeliveries().isEmpty()) {
            List<Delivery> optimized = optimizeList(tour);
            tour.setDeliveries(optimized);
        }
        Tour saved = tourRepository.save(tour);
        return tourMapper.toDto(saved);
    }

    private List<Delivery> optimizeList(Tour tour) {
        System.out.println("⚙️ optimizerType from config = " + optimizerType);

        TourOptimizer optimizer = optimizerFactory.getOptimizer(optimizerType);
        System.out.println("🔍 Using optimizer: " + optimizerType + " -> " + (optimizer == null ? "NULL" : optimizer.getClass().getSimpleName()));

        List<Delivery> optimized = optimizer.optimizerTour(tour);

        double totalDistance = TourUtils.calculateTotalDistance(
                tour.getWarehouse(),
                optimized,
                distanceCalculator
        );
        tour.setTotalDistance(totalDistance);
        return optimized;
    }

}

package org.example.smartroute.services.impl;

import org.example.smartroute.entities.DTO.DeliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.DeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.UpdateDeliveryHistoryDto;
import org.example.smartroute.entities.models.Customer;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.entities.models.DeliveryHistory;
import org.example.smartroute.entities.models.Tour;
import org.example.smartroute.mappers.DeliveryHistoryMapper;
import org.example.smartroute.repositories.CustomerRepository;
import org.example.smartroute.repositories.DeliveryRepository;
import org.example.smartroute.repositories.DeliveryHistoryRepository;
import org.example.smartroute.repositories.TourRepository;
import org.example.smartroute.services.IDeliveryHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeliveryHistoryService implements IDeliveryHistoryService {

    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;
    private final TourRepository tourRepository;
    private final DeliveryHistoryMapper mapper;

    public DeliveryHistoryService(
            DeliveryHistoryRepository deliveryHistoryRepository,
            DeliveryRepository deliveryRepository,
            CustomerRepository customerRepository,
            TourRepository tourRepository,
            DeliveryHistoryMapper mapper
    ) {
        this.deliveryHistoryRepository = deliveryHistoryRepository;
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
        this.tourRepository = tourRepository;
        this.mapper = mapper;
    }

    @Override
    public DeliveryHistoryDto create(CreateDeliveryHistoryDto dto) {
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId())
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Tour tour = tourRepository.findById(dto.getTourId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        DeliveryHistory history = mapper.toEntity(dto);
        history.setDelivery(delivery);
        history.setCustomer(customer);
        history.setTour(tour);

        DeliveryHistory saved = deliveryHistoryRepository.save(history);
        return mapper.toDto(saved);
    }

    @Override
    public DeliveryHistoryDto update(Long id, UpdateDeliveryHistoryDto dto) {
        DeliveryHistory history = deliveryHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery history not found"));

        if (dto.getPlannedTime() != null) history.setPlannedTime(dto.getPlannedTime());
        if (dto.getActualTime() != null) history.setActualTime(dto.getActualTime());
        if (dto.getDelay() != null) history.setDelay(dto.getDelay());
        if (dto.getDayOfWeek() != null) history.setDayOfWeek(dto.getDayOfWeek());
        if (dto.getTourId() != null) {
            Tour tour = tourRepository.findById(dto.getTourId())
                    .orElseThrow(() -> new RuntimeException("Tour not found"));
            history.setTour(tour);
        }

        DeliveryHistory updated = deliveryHistoryRepository.save(history);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!deliveryHistoryRepository.existsById(id)) {
            throw new RuntimeException("Delivery history not found");
        }
        deliveryHistoryRepository.deleteById(id);
    }

    @Override
    public DeliveryHistoryDto getById(Long id) {
        DeliveryHistory history = deliveryHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery history not found"));
        return mapper.toDto(history);
    }

    @Override
    public List<DeliveryHistoryDto> getAll() {
        return deliveryHistoryRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

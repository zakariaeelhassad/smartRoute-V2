package org.example.smartroute.services.impl;

import org.example.smartroute.entities.DTO.delivery.CreateDeliveryDto;
import org.example.smartroute.entities.DTO.delivery.UpdateDeliveryDto;
import org.example.smartroute.entities.DTO.delivery.DeliveryDto;
import org.example.smartroute.entities.models.Delivery;
import org.example.smartroute.mappers.DeliveryMapper;
import org.example.smartroute.repositories.DeliveryRepository;
import org.example.smartroute.services.IDeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService implements IDeliveryService {

    private final DeliveryRepository repository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryService(DeliveryRepository repository, DeliveryMapper deliveryMapper) {
        this.repository = repository;
        this.deliveryMapper = deliveryMapper;
    }

    @Override
    public DeliveryDto create(CreateDeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        Delivery savedDelivery = repository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    @Override
    public DeliveryDto update(Long id , UpdateDeliveryDto deliveryDto) {
        Delivery delivery = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setAddress(deliveryDto.address());
        delivery.setLatitude(deliveryDto.latitude());
        delivery.setLongitude(deliveryDto.longitude());
        delivery.setWeight(deliveryDto.weight());
        delivery.setVolume(deliveryDto.volume());
        delivery.setTimeWindow(deliveryDto.timeWindow());
        delivery.setDeliveryStatus(deliveryDto.deliveryStatus());

        Delivery updatedDelivery = repository.save(delivery);
        return deliveryMapper.toDto(updatedDelivery);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public DeliveryDto getById(Long id) {
        Delivery delivery = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        return deliveryMapper.toDto(delivery);
    }

    @Override
    public List<DeliveryDto> getAll() {
        return repository.findAll().stream()
                .map(deliveryMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<DeliveryDto> getAllPage(Pageable pageable) {
        return repository.findAll(pageable).map(deliveryMapper::toDto);
    }

    public Page<DeliveryDto> searchByAddress(String address, Pageable pageable) {
        return repository.findByAddressContainingIgnoreCase(address, pageable)
                .map(deliveryMapper::toDto);
    }

    public Page<DeliveryDto> searchByStatus(String status, Pageable pageable) {
        return repository.findByDeliveryStatus(status, pageable)
                .map(deliveryMapper::toDto);
    }

}

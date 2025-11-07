package org.example.smartroute.services.impl;

import org.example.smartroute.entities.DTO.warehouse.CreateWarehouseDto;
import org.example.smartroute.entities.DTO.warehouse.UpdateWarehouseDto;
import org.example.smartroute.entities.DTO.warehouse.WarehouseDto;
import org.example.smartroute.entities.models.Warehouse;
import org.example.smartroute.mappers.WarehouseMapper;
import org.example.smartroute.repositories.WarehouseRepository;
import org.example.smartroute.services.IWarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService implements IWarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper warehouseMapper;

    public WarehouseService(WarehouseRepository repository , WarehouseMapper warehouseMapper) {
        this.repository = repository;
        this.warehouseMapper = warehouseMapper ;

    }

    @Override
    public WarehouseDto create(CreateWarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDto);
        Warehouse savedWarehouse = repository.save(warehouse);
        return warehouseMapper.toDto(savedWarehouse);
    }

    @Override
    public WarehouseDto update(UpdateWarehouseDto warehouseDto, Long id) {
        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        warehouse.setName(warehouseDto.name());
        warehouse.setAddress(warehouseDto.address());
        warehouse.setLatitude(warehouseDto.latitude());
        warehouse.setLongitude(warehouseDto.longitude());

        Warehouse updatedWarehouse = repository.save(warehouse);
        return warehouseMapper.toDto(updatedWarehouse);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Warehouse not found");
        }
        repository.deleteById(id);
    }

    @Override
    public WarehouseDto getById(Long id) {
        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    public List<WarehouseDto> getAll() {
        return repository.findAll().stream()
                .map(warehouseMapper::toDto)
                .collect(Collectors.toList());
    }
}

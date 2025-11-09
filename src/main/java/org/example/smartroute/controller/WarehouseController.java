package org.example.smartroute.controller;


import jakarta.validation.Valid;
import org.example.smartroute.entities.DTO.warehouse.CreateWarehouseDto;
import org.example.smartroute.entities.DTO.warehouse.UpdateWarehouseDto;
import org.example.smartroute.entities.DTO.warehouse.WarehouseDto;
import org.example.smartroute.services.IWarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final IWarehouseService service;

    public WarehouseController(IWarehouseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WarehouseDto> createWarehouse(@RequestBody @Valid CreateWarehouseDto createWarehouseDto) {
        WarehouseDto createdWarehouse = service.create(createWarehouseDto);
        return ResponseEntity.ok(createdWarehouse);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDto>> getAllWarehouses() {
        List<WarehouseDto> warehouses = service.getAll();
        return new ResponseEntity<>(warehouses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable("id") Long id) {
        WarehouseDto warehouse = service.getById(id);
        return new ResponseEntity<>(warehouse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable("id") Long id,
                                                        @RequestBody @Valid UpdateWarehouseDto updateWarehouseDto) {
        WarehouseDto warehouse = service.update(updateWarehouseDto, id);
        return new ResponseEntity<>(warehouse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

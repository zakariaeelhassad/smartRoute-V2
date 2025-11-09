package org.example.smartroute.controller;

import jakarta.validation.Valid;
import org.example.smartroute.entities.DTO.delivery.CreateDeliveryDto;
import org.example.smartroute.entities.DTO.delivery.UpdateDeliveryDto;
import org.example.smartroute.entities.DTO.delivery.DeliveryDto;
import org.example.smartroute.services.IDeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final IDeliveryService service;

    public DeliveryController(IDeliveryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DeliveryDto> createDelivery(@RequestBody @Valid CreateDeliveryDto createDeliveryDto) {
        DeliveryDto createdDelivery = service.create(createDeliveryDto);
        return ResponseEntity.ok(createdDelivery);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        List<DeliveryDto> deliveries = service.getAll();
        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDto> getDeliveryById(@PathVariable("id") Long id) {
        DeliveryDto delivery = service.getById(id);
        return new ResponseEntity<>(delivery, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDto> updateDelivery(@PathVariable("id") Long id,
                                                      @RequestBody @Valid UpdateDeliveryDto updateDeliveryDto) {
        DeliveryDto delivery = service.update(id, updateDeliveryDto);
        return new ResponseEntity<>(delivery, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDelivery(@PathVariable("id") Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DeliveryDto>> getDeliveries(Pageable pageable) {
        return ResponseEntity.ok(service.getAllPage(pageable));
    }

    @GetMapping("/search/address")
    public ResponseEntity<Page<DeliveryDto>> searchByAddress(
            @RequestParam String address,
            Pageable pageable) {
        return ResponseEntity.ok(service.searchByAddress(address, pageable));
    }

}

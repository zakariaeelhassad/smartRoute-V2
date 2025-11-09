package org.example.smartroute.controller;

import org.example.smartroute.entities.DTO.deliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.DTO.deliveryHistory.DeliveryHistoryDto;
import org.example.smartroute.entities.DTO.deliveryHistory.UpdateDeliveryHistoryDto;
import org.example.smartroute.services.IDeliveryHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/delivery-histories")
public class DeliveryHistoryController {

    private final IDeliveryHistoryService service;

    public DeliveryHistoryController(IDeliveryHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DeliveryHistoryDto> create(@RequestBody CreateDeliveryHistoryDto dto) {
        DeliveryHistoryDto created = service.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryHistoryDto> update(@PathVariable Long id, @RequestBody UpdateDeliveryHistoryDto dto
    ) {
        DeliveryHistoryDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryHistoryDto> getById(@PathVariable Long id) {
        DeliveryHistoryDto dto = service.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryHistoryDto>> getAll() {
        List<DeliveryHistoryDto> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DeliveryHistoryDto>> getAllPaged(Pageable pageable) {
        return ResponseEntity.ok(service.getAllPage(pageable));
    }

    @GetMapping("/search/day")
    public ResponseEntity<Page<DeliveryHistoryDto>> findByDay(@RequestParam DayOfWeek day, Pageable pageable) {
        return ResponseEntity.ok(service.findByDayOfWeek(day, pageable));
    }

    @GetMapping("/search/customer")
    public ResponseEntity<Page<DeliveryHistoryDto>> findByCustomerName(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(service.findByCustomerName(name, pageable));
    }

}

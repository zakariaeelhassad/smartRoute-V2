package org.example.smartroute.controller;

import org.example.smartroute.entities.DTO.DeliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.DeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.UpdateDeliveryHistoryDto;
import org.example.smartroute.services.IDeliveryHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-histories")
public class DeliveryHistoryController {

    private final IDeliveryHistoryService deliveryHistoryService;

    public DeliveryHistoryController(IDeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryHistoryDto> create(@RequestBody CreateDeliveryHistoryDto dto) {
        DeliveryHistoryDto created = deliveryHistoryService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryHistoryDto> update(@PathVariable Long id, @RequestBody UpdateDeliveryHistoryDto dto
    ) {
        DeliveryHistoryDto updated = deliveryHistoryService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryHistoryDto> getById(@PathVariable Long id) {
        DeliveryHistoryDto dto = deliveryHistoryService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryHistoryDto>> getAll() {
        List<DeliveryHistoryDto> list = deliveryHistoryService.getAll();
        return ResponseEntity.ok(list);
    }
}

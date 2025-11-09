package org.example.smartroute.controller;

import jakarta.validation.Valid;
import org.example.smartroute.entities.DTO.tour.CreateTourDto;
import org.example.smartroute.entities.DTO.tour.TourDto;
import org.example.smartroute.entities.DTO.tour.UpdateTourDto;
import org.example.smartroute.services.ITourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final ITourService tourService;

    public TourController(ITourService tourService) {
        this.tourService = tourService;
    }

    @PostMapping
    public ResponseEntity<TourDto> create(@RequestBody @Valid CreateTourDto dto) {
        TourDto created = tourService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourDto> update(@PathVariable Long id,
                                          @RequestBody @Valid UpdateTourDto dto) {
        TourDto updated = tourService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TourDto>> getAll() {
        List<TourDto> tours = tourService.getAll();
        return ResponseEntity.ok(tours);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDto> getById(@PathVariable Long id) {
        TourDto tour = tourService.getById(id);
        return ResponseEntity.ok(tour);
    }

    @PostMapping("/{id}/optimize")
    public ResponseEntity<String> optimize(@PathVariable Long id) {
        tourService.optimizeTour(id);
        return ResponseEntity.ok("Tour optimized successfully!");
    }

    @PostMapping("/{id}/deliveries")
    public ResponseEntity<TourDto> addDeliveries(@PathVariable Long id,
                                                 @RequestBody List<Long> deliveryIds) {
        TourDto updatedTour = tourService.addDeliveriesToTour(id, deliveryIds);
        return ResponseEntity.ok(updatedTour);
    }
}

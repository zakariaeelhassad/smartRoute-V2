package org.example.smartroute.controller;

import jakarta.validation.Valid;
import org.example.smartroute.entities.DTO.vehicle.CreateVehicleDto;
import org.example.smartroute.entities.DTO.vehicle.UpdateVehicleDto;
import org.example.smartroute.entities.DTO.vehicle.VehicleDto;
import org.example.smartroute.services.IVehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private IVehicleService service ;

    public VehicleController(IVehicleService service){
        this.service = service ;
    }

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@RequestBody @Valid CreateVehicleDto createVehicleDto){
        VehicleDto createdVehicle = service.create(createVehicleDto);
        return ResponseEntity.ok(createdVehicle);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDto>> getAllVehicle(){
        List<VehicleDto> vehicles = service.getAll();
        return new ResponseEntity<>(vehicles , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable("id") Long id ){
        VehicleDto vehicle = service.getById(id);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable("id") Long id,@RequestBody @Valid UpdateVehicleDto updateVehicleDto){
        VehicleDto vehicle = service.update(updateVehicleDto , id);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

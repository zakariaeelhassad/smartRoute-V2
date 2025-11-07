    package org.example.smartroute.services.impl;

    import org.example.smartroute.entities.DTO.vehicle.CreateVehicleDto;
    import org.example.smartroute.entities.DTO.vehicle.UpdateVehicleDto;
    import org.example.smartroute.entities.DTO.vehicle.VehicleDto;
    import org.example.smartroute.entities.models.Vehicle;
    import org.example.smartroute.mappers.VehicleMapper;
    import org.example.smartroute.repositories.VehicleRepository;
    import org.example.smartroute.services.IVehicleService;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class VehicleService implements IVehicleService {

        private final VehicleRepository repository;
        private final VehicleMapper vehicleMapper;

        public VehicleService(VehicleRepository repository , VehicleMapper vehicleMapper) {
            this.repository = repository;
            this.vehicleMapper = vehicleMapper;
        }

        private void applyVehicleDefaults(Vehicle vehicle) {
            if (vehicle.getVehicleType() != null) {
                vehicle.setCapacityWeight(vehicle.getVehicleType().getCapacityWeight());
                vehicle.setCapacityVolume(vehicle.getVehicleType().getCapacityVolume());
                vehicle.setMaxDeliveries(vehicle.getVehicleType().getMaxDeliveries());
            }
        }

        @Override
        public VehicleDto create(CreateVehicleDto vehicleDto) {
            Vehicle vehicle = vehicleMapper.toEntity(vehicleDto);
            applyVehicleDefaults(vehicle);
            Vehicle savevehicle = repository.save(vehicle);
            return vehicleMapper.toDto(savevehicle);
        }

        @Override
        public VehicleDto update(UpdateVehicleDto vehicleDto, Long id) {
            Vehicle vehicle = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("vehicle not found"));

            vehicle.setName(vehicleDto.name());
            vehicle.setVehicleType(vehicleDto.vehicleType());
            applyVehicleDefaults(vehicle);

            Vehicle updateVehicle = repository.save(vehicle);
            return vehicleMapper.toDto(updateVehicle);
        }

        @Override
        public void delete(Long id) {
            repository.deleteById(id);
        }

        @Override
        public VehicleDto getById(Long id) {
            Vehicle vehicle = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("vehicle not found"));

            return vehicleMapper.toDto(vehicle);
        }

        @Override
        public List<VehicleDto> getAll() {
            List<VehicleDto> result = repository.findAll().stream()
                    .map(vehicleMapper::toDto).
                    collect(Collectors.toList());
            return result;
        }
    }

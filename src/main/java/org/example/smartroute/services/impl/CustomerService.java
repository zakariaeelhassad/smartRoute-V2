package org.example.smartroute.services.impl;

import org.example.smartroute.entities.DTO.customer.CreateCustomerDto;
import org.example.smartroute.entities.DTO.customer.UpdateCustomerDto;
import org.example.smartroute.entities.DTO.customer.CustomerDto;
import org.example.smartroute.entities.models.Customer;
import org.example.smartroute.mappers.CustomerMapper;
import org.example.smartroute.repositories.CustomerRepository;
import org.example.smartroute.services.ICustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository repository, CustomerMapper customerMapper) {
        this.repository = repository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto create(CreateCustomerDto dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer savedCustomer = repository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public CustomerDto update(Long id, UpdateCustomerDto dto) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(dto.name());
        customer.setAddress(dto.address());
        customer.setLatitude(dto.latitude());
        customer.setLongitude(dto.longitude());
        customer.setPreferredTimeSlot(dto.preferredTimeSlot());

        Customer updatedCustomer = repository.save(customer);
        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        repository.deleteById(id);
    }

    @Override
    public CustomerDto getById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> getAll() {
        return repository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }
}

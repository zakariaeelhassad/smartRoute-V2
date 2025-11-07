package org.example.smartroute.services;

import org.example.smartroute.entities.DTO.customer.CreateCustomerDto;
import org.example.smartroute.entities.DTO.customer.CustomerDto;
import org.example.smartroute.entities.DTO.customer.UpdateCustomerDto;

import java.util.List;

public interface ICustomerService {

    CustomerDto create(CreateCustomerDto customerDto);
    CustomerDto update(Long id, UpdateCustomerDto customerDto);
    void delete(Long id);
    CustomerDto getById(Long id);
    List<CustomerDto> getAll();
}

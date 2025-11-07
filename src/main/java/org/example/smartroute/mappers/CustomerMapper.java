package org.example.smartroute.mappers;

import org.example.smartroute.entities.DTO.customer.CreateCustomerDto;
import org.example.smartroute.entities.DTO.customer.CustomerDto;
import org.example.smartroute.entities.models.Customer;
import org.example.smartroute.mappers.api.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends GenericMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CreateCustomerDto dto);

    CustomerDto toDto(Customer customer);
}

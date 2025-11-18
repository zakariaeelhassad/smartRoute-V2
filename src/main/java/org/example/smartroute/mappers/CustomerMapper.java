    package org.example.smartroute.mappers;

    import org.example.smartroute.entities.DTO.customer.CreateCustomerDto;
    import org.example.smartroute.entities.DTO.customer.CustomerDto;
    import org.example.smartroute.entities.models.Customer;
    import org.example.smartroute.mappers.api.GenericMapper;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;

    import java.util.List;

    @Mapper(componentModel = "spring")
    public interface CustomerMapper extends GenericMapper {
        @Mapping(target = "id", ignore = true)
        Customer toEntity(CreateCustomerDto dto);

        @Mapping(target = "deliveryIds", expression = "java(mapDeliveryIds(customer))")
        CustomerDto toDto(Customer customer);

        default List<Long> mapDeliveryIds(Customer customer) {
            if (customer.getDeliveries() == null) return List.of();
            return customer.getDeliveries()
                    .stream()
                    .map(d -> d.getId())
                    .toList();
        }
    }

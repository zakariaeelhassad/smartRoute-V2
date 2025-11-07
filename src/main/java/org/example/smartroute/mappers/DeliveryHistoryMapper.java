package org.example.smartroute.mappers;

import org.example.smartroute.entities.DTO.DeliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.DeliveryHistoryDto;
import org.example.smartroute.entities.models.DeliveryHistory;
import org.example.smartroute.mappers.api.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryHistoryMapper extends GenericMapper {

    @Mapping(target = "id", ignore = true)
    DeliveryHistory toEntity(CreateDeliveryHistoryDto dto);

    DeliveryHistoryDto toDto(DeliveryHistory deliveryHistory);
}

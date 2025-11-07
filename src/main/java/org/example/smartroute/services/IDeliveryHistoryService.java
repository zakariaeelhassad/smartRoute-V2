package org.example.smartroute.services;

import org.example.smartroute.entities.DTO.DeliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.DeliveryHistoryDto;
import org.example.smartroute.entities.DTO.DeliveryHistory.UpdateDeliveryHistoryDto;

import java.util.List;

public interface IDeliveryHistoryService {
    DeliveryHistoryDto create(CreateDeliveryHistoryDto dto);
    DeliveryHistoryDto update(Long id, UpdateDeliveryHistoryDto dto);
    void delete(Long id);
    DeliveryHistoryDto getById(Long id);
    List<DeliveryHistoryDto> getAll();
}

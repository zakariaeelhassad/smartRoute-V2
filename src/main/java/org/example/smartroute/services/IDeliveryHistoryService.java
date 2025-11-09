package org.example.smartroute.services;

import org.example.smartroute.entities.DTO.deliveryHistory.CreateDeliveryHistoryDto;
import org.example.smartroute.entities.DTO.deliveryHistory.DeliveryHistoryDto;
import org.example.smartroute.entities.DTO.deliveryHistory.UpdateDeliveryHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.util.List;

public interface IDeliveryHistoryService {
    DeliveryHistoryDto create(CreateDeliveryHistoryDto dto);
    DeliveryHistoryDto update(Long id, UpdateDeliveryHistoryDto dto);
    void delete(Long id);
    DeliveryHistoryDto getById(Long id);
    List<DeliveryHistoryDto> getAll();
    Page<DeliveryHistoryDto> getAllPage(Pageable pageable);
    Page<DeliveryHistoryDto> findByDayOfWeek(DayOfWeek dayOfWeek, Pageable pageable);
    Page<DeliveryHistoryDto> findByCustomerName(String name, Pageable pageable);
}

package org.example.smartroute.entities.DTO.customer;

import java.util.List;

public record CustomerDto(

        Long id,
        String name,
        String address,
        Double latitude,
        Double longitude,
        String preferredTimeSlot,
        List<Long> deliveryIds

) {}

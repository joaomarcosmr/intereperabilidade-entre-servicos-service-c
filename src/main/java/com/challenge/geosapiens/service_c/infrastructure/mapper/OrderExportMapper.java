package com.challenge.geosapiens.service_c.infrastructure.mapper;

import com.challenge.geosapiens.service_c.infrastructure.dto.OrderExportDTO;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderExportMapper {

    public OrderExportDTO toOrderExportDTO(OrderResponseDTO order) {
        if (order == null) {
            log.warn("[OrderExportMapper] Order is null, cannot map");
            return null;
        }

        return new OrderExportDTO(
            order.getId().toString(),
            order.getDescription(),
            order.getValue(),
            order.getDeliveryPersonName(),
            order.getDeliveryPersonPhone(),
            order.getUser().getName(),
            order.getUser().getEmail()
        );
    }
}

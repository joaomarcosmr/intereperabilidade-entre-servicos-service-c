package com.challenge.geosapiens.service_c.domain.usecase;

import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;

import java.util.List;

public interface GetOrdersUseCase {
    List<OrderResponseDTO> execute();
}

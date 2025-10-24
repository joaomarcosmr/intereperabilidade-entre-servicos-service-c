package com.challenge.geosapiens.service_c.infrastructure.client;

import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "order-service", url = "http://localhost:8081")
public interface OrderServiceClient {

    @GetMapping("/order")
    List<OrderResponseDTO> getAllOrders();
}

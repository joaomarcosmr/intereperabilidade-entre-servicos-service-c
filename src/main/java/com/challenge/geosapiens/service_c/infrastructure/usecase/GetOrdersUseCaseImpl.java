package com.challenge.geosapiens.service_c.infrastructure.usecase;

import com.challenge.geosapiens.service_c.domain.usecase.GetOrdersUseCase;
import com.challenge.geosapiens.service_c.infrastructure.client.OrderServiceClient;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetOrdersUseCaseImpl implements GetOrdersUseCase {

    private final OrderServiceClient orderServiceClient;

    @Override
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public List<OrderResponseDTO> execute() {
        log.info("[GetOrdersUseCase] Calling external order service...");
        try {
            List<OrderResponseDTO> orders = orderServiceClient.getAllOrders();
            log.info("[GetOrdersUseCase] Successfully retrieved {} orders", orders.size());
            return orders;
        } catch (Exception e) {
            log.error("[GetOrdersUseCase] Error calling order service: {}", e.getMessage());
            throw e;
        }
    }

    @Recover
    public List<OrderResponseDTO> recoverExecute(Exception e) {
        log.error("[GetOrdersUseCase] All retry attempts failed. Returning empty list. Error: {}", e.getMessage());
        return Collections.emptyList();
    }
}

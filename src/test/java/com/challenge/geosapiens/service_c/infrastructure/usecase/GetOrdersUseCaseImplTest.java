package com.challenge.geosapiens.service_c.infrastructure.usecase;

import com.challenge.geosapiens.service_c.infrastructure.client.OrderServiceClient;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;
import com.challenge.geosapiens.service_c.infrastructure.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrdersUseCaseImplTest {

    @Mock
    private OrderServiceClient orderServiceClient;

    @InjectMocks
    private GetOrdersUseCaseImpl getOrdersUseCase;

    private OrderResponseDTO orderResponseDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userResponseDTO = new UserResponseDTO(
            UUID.randomUUID(),
            "Teste João Marcos",
            "joaomarcos@teste.com",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        orderResponseDTO = new OrderResponseDTO(
            UUID.randomUUID(),
            "Pedido de Camisas do Botafogo",
            100.0,
            userResponseDTO,
            "Loco Abreu",
            "47991470224",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    void shouldReturnOrdersWhenClientReturnsSuccessfully() {
        List<OrderResponseDTO> expectedOrders = Arrays.asList(orderResponseDTO);
        when(orderServiceClient.getAllOrders()).thenReturn(expectedOrders);

        List<OrderResponseDTO> result = getOrdersUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderResponseDTO.getDescription(), result.get(0).getDescription());
        verify(orderServiceClient, times(1)).getAllOrders();
    }

    @Test
    void shouldReturnEmptyListWhenClientReturnsEmptyList() {
        when(orderServiceClient.getAllOrders()).thenReturn(Arrays.asList());

        List<OrderResponseDTO> result = getOrdersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderServiceClient, times(1)).getAllOrders();
    }

    @Test
    void shouldThrowExceptionWhenClientFails() {
        when(orderServiceClient.getAllOrders()).thenThrow(new RuntimeException("Error fetching orders"));

        assertThrows(RuntimeException.class, () -> getOrdersUseCase.execute());
        verify(orderServiceClient, times(1)).getAllOrders();
    }
}

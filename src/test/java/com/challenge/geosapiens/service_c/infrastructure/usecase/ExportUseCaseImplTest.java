package com.challenge.geosapiens.service_c.infrastructure.usecase;

import com.challenge.geosapiens.service_c.domain.usecase.GetOrdersUseCase;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderExportDTO;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;
import com.challenge.geosapiens.service_c.infrastructure.dto.UserResponseDTO;
import com.challenge.geosapiens.service_c.infrastructure.mapper.OrderExportMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportUseCaseImplTest {

    @Mock
    private GetOrdersUseCase getOrdersUseCase;

    @Mock
    private OrderExportMapper orderExportMapper;

    @InjectMocks
    private ExportUseCaseImpl exportUseCase;

    private OrderResponseDTO orderResponseDTO;
    private OrderExportDTO orderExportDTO;
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

        orderExportDTO = new OrderExportDTO(
                orderResponseDTO.getId().toString(),
            "Pedido de Camisas do Botafogo",
            100.0,
            "Loco Abreu",
            "47991470224",
            "Teste João Marcos",
            "joaomarcos@teste.com"
        );
    }

    @Test
    void shouldGenerateCSVWhenOrdersAreRetrievedSuccessfully() {
        List<OrderResponseDTO> orders = Arrays.asList(orderResponseDTO);
        when(getOrdersUseCase.execute()).thenReturn(orders);
        when(orderExportMapper.toOrderExportDTO(any(OrderResponseDTO.class))).thenReturn(orderExportDTO);

        String result = exportUseCase.execute();

        assertNotNull(result);
        assertTrue(result.contains("ID Pedido;Descrição;Valor;Nome Entregador;Telefone Entregador;Nome Usuário;Email Usuário"));
        assertTrue(result.contains("Pedido de Camisas do Botafogo"));
        assertTrue(result.contains("Loco Abreu"));
        assertTrue(result.contains("joaomarcos@teste.com"));
        verify(getOrdersUseCase, times(1)).execute();
        verify(orderExportMapper, times(1)).toOrderExportDTO(any(OrderResponseDTO.class));
    }

    @Test
    void shouldReturnCSVHeaderWhenNoOrdersFound() {
        when(getOrdersUseCase.execute()).thenReturn(Collections.emptyList());

        String result = exportUseCase.execute();

        assertNotNull(result);
        assertTrue(result.contains("ID Pedido;Descrição;Valor;Nome Entregador;Telefone Entregador;Nome Usuário;Email Usuário"));
        assertEquals(1, result.split("\n").length);
        verify(getOrdersUseCase, times(1)).execute();
        verify(orderExportMapper, never()).toOrderExportDTO(any());
    }

    @Test
    void shouldFilterOutOrdersWithNullMapping() {
        List<OrderResponseDTO> orders = Arrays.asList(orderResponseDTO, orderResponseDTO);
        when(getOrdersUseCase.execute()).thenReturn(orders);
        when(orderExportMapper.toOrderExportDTO(any(OrderResponseDTO.class)))
            .thenReturn(orderExportDTO)
            .thenReturn(null);

        String result = exportUseCase.execute();

        assertNotNull(result);
        assertTrue(result.contains("Pedido de Camisas do Botafogo"));
        assertEquals(2, result.split("\n").length);
        verify(getOrdersUseCase, times(1)).execute();
        verify(orderExportMapper, times(2)).toOrderExportDTO(any(OrderResponseDTO.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenGetOrdersFails() {
        when(getOrdersUseCase.execute()).thenThrow(new RuntimeException("Error fetching orders"));

        assertThrows(RuntimeException.class, () -> exportUseCase.execute());
        verify(getOrdersUseCase, times(1)).execute();
        verify(orderExportMapper, never()).toOrderExportDTO(any());
    }
}

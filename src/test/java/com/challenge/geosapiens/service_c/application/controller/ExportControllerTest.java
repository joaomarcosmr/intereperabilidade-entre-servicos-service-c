package com.challenge.geosapiens.service_c.application.controller;

import com.challenge.geosapiens.service_c.domain.usecase.ExportUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportControllerTest {

    @Mock
    private ExportUseCase exportUseCase;

    @InjectMocks
    private ExportController exportController;

    @Test
    void shouldReturnCSVFileWhenExportOrdersIsSuccessful() {
        String csvContent = "ID Pedido,Descricao,Valor,Nome Entregador,Telefone Entregador,Nome Usuario,Email Usuario\n" +
                           "123,Pedido testes,100.0,Entregador teste,47996359003,Joao Marcos,joaomarcos@teste.com\n";
        when(exportUseCase.execute()).thenReturn(csvContent);

        ResponseEntity<String> response = exportController.exportOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(csvContent, response.getBody());
        assertEquals(MediaType.parseMediaType("text/csv; charset=UTF-8"), response.getHeaders().getContentType());
        assertEquals("attachment; filename=orders.csv", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(exportUseCase, times(1)).execute();
    }

    @Test
    void shouldReturnEmptyCSVWhenNoOrdersFound() {
        String emptyCSV = "ID Pedido,Descricao,Valor,Nome Entregador,Telefone Entregador,Nome Usuario,Email Usuario\n";
        when(exportUseCase.execute()).thenReturn(emptyCSV);

        ResponseEntity<String> response = exportController.exportOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyCSV, response.getBody());
        assertEquals(MediaType.parseMediaType("text/csv; charset=UTF-8"), response.getHeaders().getContentType());
        assertEquals("attachment; filename=orders.csv", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(exportUseCase, times(1)).execute();
    }
}

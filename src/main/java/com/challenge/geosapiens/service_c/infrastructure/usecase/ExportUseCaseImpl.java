package com.challenge.geosapiens.service_c.infrastructure.usecase;

import com.challenge.geosapiens.service_c.domain.usecase.ExportUseCase;
import com.challenge.geosapiens.service_c.domain.usecase.GetOrdersUseCase;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderExportDTO;
import com.challenge.geosapiens.service_c.infrastructure.dto.OrderResponseDTO;
import com.challenge.geosapiens.service_c.infrastructure.mapper.OrderExportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportUseCaseImpl implements ExportUseCase {

    private final GetOrdersUseCase getOrdersUseCase;
    private final OrderExportMapper orderExportMapper;

    @Override
    public String execute() {
        try {
            log.info("[ExportUseCase] Searching orders from order service...");
            List<OrderResponseDTO> orders = getOrdersUseCase.execute();
            log.info("[ExportUseCase] Order list received: {}", orders.size());

            List<OrderExportDTO> exportData = orders.stream()
                .map(orderExportMapper::toOrderExportDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            if (exportData.size() < orders.size()) {
                log.warn("[ExportUseCase] {} orders were filtered out due to missing user information",
                    orders.size() - exportData.size());
            }

            return convertToCSV(exportData);
        } catch (Exception e) {
            log.error("[ExportUseCase] Error fetching orders: {}", e.getMessage());
            throw new RuntimeException("Error fetching orders", e.getCause());
        }
    }

    private String convertToCSV(List<OrderExportDTO> orders) {
        log.info("[ExportUseCase] Converting order list received to CSV format...");
        StringBuilder csv = new StringBuilder();

        // Add UTF-8 BOM for proper encoding recognition in Excel
        csv.append("\uFEFF");

        // Use semicolon as separator (Windows/Excel standard for Portuguese locale)
        csv.append("ID Pedido;Descrição;Valor;Nome Entregador;Telefone Entregador;Nome Usuário;Email Usuário\n");

        for (OrderExportDTO order : orders) {
            csv.append(order.getOrderId()).append(";")
                    .append("\"").append(order.getDescription()).append("\"").append(";")
                    .append(order.getValue()).append(";")
                    .append("\"").append(order.getDeliveryPersonName()).append("\"").append(";")
                    .append("\"").append(order.getDeliveryPersonPhone()).append("\"").append(";")
                    .append("\"").append(order.getUserName()).append("\"").append(";")
                    .append("\"").append(order.getUserEmail()).append("\"")
                    .append("\n");
        }

        log.info("[ExportUseCase] Converted order list received to CSV format: {}", csv);
        return csv.toString();
    }
}

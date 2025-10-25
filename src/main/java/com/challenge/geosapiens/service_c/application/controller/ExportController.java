package com.challenge.geosapiens.service_c.application.controller;

import com.challenge.geosapiens.service_c.domain.usecase.ExportUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
@Tag(name = "Export", description = "Endpoints for exporting data with csv")
public class ExportController {

    private final ExportUseCase exportUseCase;

    @GetMapping("/orders")
    @Operation(
            summary = "Export orders to CSV",
            description = "Exports all orders with user information to a CSV file. The file includes order ID, user ID, user name, user email, total amount, and order status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file successfully generated"
            )
    })
    public ResponseEntity<String> exportOrders() {
        String csv = exportUseCase.execute();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }
}

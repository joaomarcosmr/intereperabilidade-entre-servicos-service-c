package com.challenge.geosapiens.service_c.application.controller;

import com.challenge.geosapiens.service_c.domain.usecase.ExportUseCase;
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
public class ExportController {

    private final ExportUseCase exportUseCase;

    @GetMapping("/orders")
    public ResponseEntity<String> exportOrders() {
        String csv = exportUseCase.execute();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv);
    }
}

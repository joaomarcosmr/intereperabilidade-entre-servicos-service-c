package com.challenge.geosapiens.service_c.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID id;
    private String description;
    private Double value;
    private UserResponseDTO user;
    private String deliveryPersonName;
    private String deliveryPersonPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

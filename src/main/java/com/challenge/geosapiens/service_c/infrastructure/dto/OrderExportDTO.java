package com.challenge.geosapiens.service_c.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderExportDTO {
    private String orderId;
    private String description;
    private Double value;
    private String deliveryPersonName;
    private String deliveryPersonPhone;
    private String userName;
    private String userEmail;
}

package com.example.cu_helper_be.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long price;
    private Long quantity;
    private Boolean isReceived;
    private String createdAt;
}

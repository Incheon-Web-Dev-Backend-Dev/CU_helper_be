package com.example.cu_helper_be.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private String name;
    private Long consumerPrice;
    private Boolean isEvent;
    private Short eventType;
    private Long eventPrice;
    private Long quantity;
    private String createdAt;
    private String image;
    private String description;
}

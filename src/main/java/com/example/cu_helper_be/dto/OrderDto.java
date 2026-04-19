package com.example.cu_helper_be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDto {
    private Long id;
    private Long orderNumber;
    private String name;
    private String phone;
    private String password;
    private Long totalPrice;
    private Boolean isPaid;
    private String createdAt;
    private Short storeCode;
}

package com.example.cu_helper_be.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDto {
    // DB 컬럼명과 동일하게 해야 함
    // @JsonProperty("컬럼명") <- annotation 추가하면 오류 발생 방지
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

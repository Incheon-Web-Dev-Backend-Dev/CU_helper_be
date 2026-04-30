package com.example.cu_helper_be.dto.response;

import com.example.cu_helper_be.dto.OrderDto;
import com.example.cu_helper_be.dto.OrderItemDto;
import lombok.*;

import java.util.List;

/*** 주문 상세조회 응답 DTO - 주문 기본 정보 + 주문 상품 목록 ***/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetailResponse {

    /*** 주문 기본 정보 (password 필드는 WRITE_ONLY로 응답에 미포함) ***/
    private OrderDto order;

    /*** 주문 상품 목록 ***/
    private List<OrderItemDto> items;
}

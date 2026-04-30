package com.example.cu_helper_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/*** 주문 상품 DTO - orderItems 테이블 스키마 기준 ***/
/*** 요청(POST) 시 사용 가능한 필드: productId, price, quantity, eventType ***/
/*** 응답 시 반환되는 필드: id, orderId, productId, price, quantity, isReceived, createdAt ***/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDto {

    /*** PK - DB 자동 생성 ***/
    private Long id;

    /*** 주문 ID (orders 테이블 FK) ***/
    private Long orderId;

    /*** 상품 ID (products 테이블 FK) ***/
    private Long productId;

    /*** 상품 단가 (소비자가격, 이벤트 할인 전) ***/
    private Long price;

    /*** 주문 수량 (이벤트 무료 수량 포함) ***/
    private Long quantity;

    /*** 수령 여부 - DB 기본값 false ***/
    private Boolean isReceived;

    /*** 생성 일시 - DB 자동 생성 ***/
    private String createdAt;

    /*** 이벤트 타입 - 서버 가격 검증용 요청 전용 필드 (DB 저장 안 함) ***/
    /*** 0: 행사없음, 1: 1+1, 2: 2+1 ***/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Short eventType;
}

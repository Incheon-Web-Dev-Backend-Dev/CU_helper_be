package com.example.cu_helper_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/*** 주문 DTO - orders 테이블 스키마 기준 ***/
/*** 요청(POST) 시 사용 가능한 필드: name, phone, password, totalPrice, storeCode, items ***/
/*** 응답 시 반환되는 필드: id, orderNumber, name, phone, totalPrice, isPaid, createdAt, storeCode ***/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    /*** PK - DB 자동 생성 ***/
    private Long id;

    /*** 주문 번호 - 서버 생성(timestamp) ***/
    private Long orderNumber;

    /*** 예약자 성명 ***/
    private String name;

    /*** 전화번호 (형식: 010-0000-0000) ***/
    private String phone;

    /*** 주문확인 비밀번호 (숫자 4자리) - 요청 수신 및 내부 검증 전용, 응답 미포함 ***/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /*** 결제 예정 총 금액 (이벤트 할인 반영) ***/
    private Long totalPrice;

    /*** 결제 완료 여부 - DB 기본값 false ***/
    private Boolean isPaid;

    /*** 생성 일시 - DB 자동 생성 ***/
    private String createdAt;

    /*** 매장 코드 (1: 장현루벤시아21단지점, 2: 은계꽃길점) ***/
    private Short storeCode;

    /*** 주문 상품 목록 - 요청 전용(orderItems 테이블에 별도 저장), 응답 미포함 ***/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<OrderItemDto> items;
}

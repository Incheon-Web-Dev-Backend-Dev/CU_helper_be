package com.example.cu_helper_be.dto;

import lombok.*;

/*** 주문 상세조회 요청 DTO - 예약자 본인 인증용 ***/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
public class OrderDetailRequest {

    /*** 예약자 성명 ***/
    private String name;

    /*** 전화번호 (형식: 010-0000-0000) ***/
    private String phone;

    /*** 주문확인 비밀번호 (숫자 4자리) ***/
    private String password;
}

package com.example.cu_helper_be.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/*** 상품 DTO - products 테이블 스키마 기준 ***/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    /*** PK - DB 자동 생성 ***/
    private Long id;

    /*** 상품명 ***/
    private String name;

    /*** 소비자 가격 ***/
    private Long consumerPrice;

    /*** 행사 여부 ***/
    private Boolean isEvent;

    /*** 행사 타입 (0: 없음, 1: 1+1, 2: 2+1) ***/
    private Short eventType;

    /*** 행사 가격 (행사 적용 시 기준 금액, nullable) ***/
    private Long eventPrice;

    /*** 재고 수량 ***/
    private Long quantity;

    /*** 생성 일시 - DB 자동 생성 ***/
    private String createdAt;

    /*** 상품 이미지 URL (nullable) ***/
    private String image;

    /*** 상품 설명 (nullable) ***/
    private String description;

    /*** 바코드 번호 ***/
    private Long barcode;
}

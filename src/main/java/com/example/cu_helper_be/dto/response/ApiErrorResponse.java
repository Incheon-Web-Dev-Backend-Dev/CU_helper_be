package com.example.cu_helper_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*** 공통 API 에러 응답 DTO ***/
@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    /*** HTTP 상태 코드 ***/
    private int status;

    /*** 사용자에게 전달할 에러 메시지 ***/
    private String message;
}

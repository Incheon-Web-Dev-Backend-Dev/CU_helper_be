package com.example.cu_helper_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/*** 관리자 로그인 응답 DTO - 인증 성공 시 이후 요청에 사용할 adminKey 반환 ***/
@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private String adminKey;
}

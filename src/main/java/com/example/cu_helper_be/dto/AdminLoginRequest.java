package com.example.cu_helper_be.dto;

import lombok.Data;

/*** 관리자 로그인 요청 DTO ***/
@Data
public class AdminLoginRequest {
    private String password;
}

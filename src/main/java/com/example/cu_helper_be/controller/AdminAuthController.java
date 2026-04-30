package com.example.cu_helper_be.controller;

import com.example.cu_helper_be.dto.AdminLoginRequest;
import com.example.cu_helper_be.dto.response.AdminLoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*** 관리자 인증 컨트롤러 ***/
@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    /*** application.properties → admin.key (환경변수 ADMIN_KEY 우선 적용) ***/
    @Value("${admin.key}")
    private String adminKey;

    /*** 관리자 로그인 - 비밀번호 검증 후 관리자 키 반환
     * 클라이언트는 반환된 adminKey를 이후 요청의 X-Admin-Key 헤더에 포함해야 합니다 ***/
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(
            @RequestBody AdminLoginRequest request,
            HttpServletRequest httpRequest) {
        log.info("admin login attempt");
        if (!adminKey.equals(request.getPassword())) {
            String ip = resolveClientIp(httpRequest);
            log.warn("IP주소: {} 관리자 비밀번호 인증 실패", ip);
            throw new IllegalArgumentException("관리자 비밀번호가 일치하지 않습니다.");
        }
        log.info("admin login success");
        return ResponseEntity.ok(new AdminLoginResponse(adminKey));
    }

    /*** 실제 클라이언트 IP 추출 - 프록시 환경에서는 X-Forwarded-For 헤더 우선 적용 ***/
    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

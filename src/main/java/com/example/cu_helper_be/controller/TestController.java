package com.example.cu_helper_be.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @PostMapping("/ping")
    public ResponseEntity<Map<String, String>> pingPost() {
        log.info("연결 테스트 성공");

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "서버 연결 성공 (POST)"
        ));
    }
}
package com.example.cu_helper_be;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class CuHelperBeApplicationStart {

    private static final Logger log =
            LoggerFactory.getLogger(CuHelperBeApplicationStart.class);

    public static void main(String[] args) {

        // 서버 시작 시간 기록
        long start = System.currentTimeMillis();
        LocalDateTime bootTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        try {
            // Spring Boot 실행
            SpringApplication.run(CuHelperBeApplicationStart.class, args);

            long end = System.currentTimeMillis();

            // 서버 시작 로그
            log.info("======================================");
            log.info("Server Started Successfully");
            log.info("Boot Time   : {}", bootTime);
            log.info("Start Time  : {} ms", (end - start));
            log.info("Status      : OK");
            log.info("======================================");

        } catch (Exception e) {
            // 서버 시작 실패 로그
            log.error("Server failed to start", e);
        }
    }
}
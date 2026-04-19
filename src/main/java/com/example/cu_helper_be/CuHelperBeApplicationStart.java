package com.example.cu_helper_be;

import com.example.cu_helper_be.dto.OrderDto;
import com.example.cu_helper_be.dto.ProductDto;
import com.example.cu_helper_be.repository.OrderRepository;
import com.example.cu_helper_be.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@SpringBootApplication
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

    @Bean
    public CommandLineRunner run(ProductRepository productRepository, OrderRepository orderRepository) {
        return args -> {

            // ======= Product 조회 =======
            log.info("======================================");
            log.info("Product 전체 조회 시작");
            log.info("======================================");

            List<ProductDto> products = productRepository.findAll();

            if (products.isEmpty()) {
                log.info("조회된 상품이 없습니다.");
            } else {
                products.forEach(p -> log.info("Product : {}", p));
            }

            log.info("총 {}개 상품 조회 완료", products.size());

            // ======= Order 조회 =======
            log.info("======================================");
            log.info("Order 전체 조회 시작");
            log.info("======================================");

            List<OrderDto> orders = orderRepository.findAll();

            if (orders.isEmpty()) {
                log.info("조회된 주문이 없습니다.");
            } else {
                orders.forEach(o -> log.info("Order : {}", o));
            }

            log.info("총 {}개 주문 조회 완료", orders.size());
            log.info("======================================");
        };
    }
}
package com.example.cu_helper_be.controller;

import com.example.cu_helper_be.dto.OrderDetailRequest;
import com.example.cu_helper_be.dto.OrderDto;
import com.example.cu_helper_be.dto.response.OrderDetailResponse;
import com.example.cu_helper_be.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*** 주문 컨트롤러 ***/
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /*** application.properties → admin.key (환경변수 ADMIN_KEY 우선 적용) ***/
    @Value("${admin.key}")
    private String adminKey;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /*** 주문 생성 - 예약자 정보 + 상품 목록 저장 ***/
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto request) {
        OrderDto createdOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /*** 관리자 전체 주문 조회 - X-Admin-Key 인증 후 전체 주문 및 상품 목록 반환
     * 관리자 페이지에서 호출 시 X-Admin-Key: {ADMIN_KEY} 헤더를 함께 전송해야 합니다 (POST /api/admin/login 응답값 사용) ***/
    @GetMapping("/admin")
    public ResponseEntity<List<OrderDetailResponse>> getAllOrdersForAdmin(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        boolean isAdmin = this.adminKey.equals(adminKey);
        List<OrderDetailResponse> orders = orderService.getAllOrdersForAdmin(isAdmin);
        return ResponseEntity.ok(orders);
    }

    /*** 주문 상세조회 - 전화번호 + 예약자 인증 후 주문 및 상품 목록 반환
     * 관리자 페이지에서 호출 시 X-Admin-Key: {ADMIN_KEY} 헤더를 함께 전송해야 인증 검증이 생략 (POST /api/admin/login 응답값 사용) ***/
    @PostMapping("/detail")
    public ResponseEntity<OrderDetailResponse> getOrderDetail(
            @RequestBody OrderDetailRequest request,
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        boolean isAdmin = this.adminKey.equals(adminKey);
        OrderDetailResponse detail = orderService.getOrderDetail(request, isAdmin);
        return ResponseEntity.ok(detail);
    }

}

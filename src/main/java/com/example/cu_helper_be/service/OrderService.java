package com.example.cu_helper_be.service;

import com.example.cu_helper_be.dto.OrderDetailRequest;
import com.example.cu_helper_be.dto.response.OrderDetailResponse;
import com.example.cu_helper_be.dto.OrderDto;
import com.example.cu_helper_be.dto.OrderItemDto;
import com.example.cu_helper_be.repository.OrderItemRepository;
import com.example.cu_helper_be.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/*** 주문 서비스 - 주문 생성 비즈니스 로직 ***/
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll();
    }

    /*** 관리자 전체 주문 조회 - 전체 주문 목록과 각 주문의 상품 목록을 함께 반환
     * isAdmin=false 이면 권한 예외 발생, X-Admin-Key 인증 필요
     * (관리자 페이지에서 X-Admin-Key: WY_ADMIN_KEY_2026A 헤더를 서버 호출 시 함께 보내세요) ***/
    public List<OrderDetailResponse> getAllOrdersForAdmin(boolean isAdmin) {
        log.info("getAllOrdersForAdmin() called - isAdmin: {}", isAdmin);
        if (!isAdmin) {
            throw new IllegalArgumentException("관리자 권한이 필요합니다.");
        }
        try {
            List<OrderDto> orders = orderRepository.findAll();

            List<OrderDetailResponse> result = orders.stream()
                    .map(order -> {
                        List<OrderItemDto> items = orderItemRepository.findByOrderId(order.getId());
                        return new OrderDetailResponse(order, items);
                    })
                    .collect(Collectors.toList());

            log.info("getAllOrdersForAdmin() success - orderCount: {}", result.size());
            return result;

        } catch (IllegalArgumentException e) {
            log.warn("getAllOrdersForAdmin() auth failed");
            throw e;
        } catch (Exception e) {
            log.error("getAllOrdersForAdmin() failed - error: {}", e.getMessage());
            throw e;
        }
    }

    /*** 주문 생성 - 행사 가격 검증 후 주문 및 주문 상품 저장 ***/
    public OrderDto createOrder(OrderDto request) {
        log.info("createOrder() called - storeCode: {}, totalPrice: {}, itemCount: {}",
                request.getStoreCode(), request.getTotalPrice(),
                request.getItems() != null ? request.getItems().size() : 0);
        try {
            validateOrderItems(request.getItems());
            validateTotalPrice(request);

            OrderDto createdOrder = orderRepository.save(request);

            if (request.getItems() != null && !request.getItems().isEmpty()) {
                orderItemRepository.saveAll(request.getItems(), createdOrder.getId());
                log.info("createOrder() items saved - orderId: {}, itemCount: {}",
                        createdOrder.getId(), request.getItems().size());
            }

            log.info("createOrder() success - orderId: {}", createdOrder.getId());
            return createdOrder;

        } catch (IllegalArgumentException e) {
            log.warn("createOrder() validation failed - storeCode: {}, reason: {}",
                    request.getStoreCode(), e.getMessage());
            throw e;
        } catch (HttpClientErrorException.Conflict e) {
            /*** 전화번호 중복 예약: 의도된 비즈니스 제약 (UNIQUE phone), WARN 레벨 처리 ***/
            log.warn("createOrder() conflict - storeCode: {}, reason: {}",
                    request.getStoreCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("createOrder() failed - storeCode: {}, error: {}",
                    request.getStoreCode(), e.getMessage());
            throw e;
        }
    }

    /*** 주문 상세조회 - 전화번호로 주문을 찾고 예약자 본인 인증 후 상세 반환
     * 관리자 호출 시 isAdmin=true 로 전달하면 이름/비밀번호 검증을 건너뜁니다.
     * (관리자 페이지에서 X-Admin-Key: WY_ADMIN_KEY_2026A 헤더를 서버 호출 시 함께 보내세요) ***/
    public OrderDetailResponse getOrderDetail(OrderDetailRequest request, boolean isAdmin) {
        log.info("getOrderDetail() called - phone: {}, isAdmin: {}", request.getPhone(), isAdmin);
        try {
            OrderDto order = orderRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new NoSuchElementException("예약 내역을 찾을 수 없습니다."));

            if (!isAdmin) {
                validateCredentials(request, order);
            }

            List<OrderItemDto> items = orderItemRepository.findByOrderId(order.getId());

            log.info("getOrderDetail() success - orderId: {}, itemCount: {}", order.getId(), items.size());
            return new OrderDetailResponse(order, items);

        } catch (NoSuchElementException e) {
            log.warn("getOrderDetail() not found - phone: {}", request.getPhone());
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("getOrderDetail() auth failed - phone: {}, reason: {}", request.getPhone(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("getOrderDetail() failed - phone: {}, error: {}", request.getPhone(), e.getMessage());
            throw e;
        }
    }

    /*** 예약자 본인 인증 - 이름과 비밀번호가 DB 저장값과 일치하는지 확인 ***/
    private void validateCredentials(OrderDetailRequest request, OrderDto order) {
        boolean nameMatch = request.getName().trim().equals(order.getName());
        boolean passwordMatch = request.getPassword().equals(order.getPassword());
        if (!nameMatch || !passwordMatch) {
            throw new IllegalArgumentException("예약자 정보가 일치하지 않습니다.");
        }
    }

    /*** 주문 상품 목록 기본 검증 ***/
    private void validateOrderItems(List<OrderItemDto> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 없습니다.");
        }
        for (OrderItemDto item : items) {
            if (item.getProductId() == null || item.getPrice() == null || item.getQuantity() == null) {
                throw new IllegalArgumentException("주문 상품 정보가 올바르지 않습니다.");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("주문 수량은 1개 이상이어야 합니다.");
            }
        }
    }

    /*** 행사 상품 가격 검증 - 프론트엔드 calcItemTotal 로직과 동일하게 서버에서 재계산 후 비교 ***/
    private void validateTotalPrice(OrderDto request) {
        long calculatedTotal = request.getItems().stream()
                .mapToLong(item -> calculateItemTotal(item.getPrice(), item.getQuantity(), item.getEventType()))
                .sum();

        if (!request.getTotalPrice().equals(calculatedTotal)) {
            throw new IllegalArgumentException(
                    String.format("총 가격이 일치하지 않습니다. (계산값: %d원, 요청값: %d원)",
                            calculatedTotal, request.getTotalPrice()));
        }
    }

    /*** 이벤트 타입별 실제 결제 금액 계산 (프론트엔드 calcItemTotal과 동일 로직) ***/
    /*** eventType 0: 일반, 1: 1+1 (홀수 개수→ ceil(qty/2)×price), 2: 2+1 (매 3개당 1개 무료) ***/
    private long calculateItemTotal(Long price, Long qty, Short eventType) {
        if (eventType == null || eventType == 0) {
            return price * qty;
        }
        if (eventType == 1) {
            /*** 1+1: 2개당 1개 요금 (올림 적용) ***/
            return (long) Math.ceil(qty / 2.0) * price;
        }
        if (eventType == 2) {
            /*** 2+1: 3개당 1개 무료 (3개 묶음에서 내림 적용) ***/
            return (qty - (qty / 3)) * price;
        }
        return price * qty;
    }
}

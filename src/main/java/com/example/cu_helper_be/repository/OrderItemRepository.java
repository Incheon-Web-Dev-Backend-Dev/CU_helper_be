package com.example.cu_helper_be.repository;

import com.example.cu_helper_be.config.SupabaseConfig;
import com.example.cu_helper_be.dto.OrderItemDto;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/*** 주문 상품 레포지토리 - Supabase REST API HTTP 통신 ***/
@Repository
public class OrderItemRepository {

    private final RestTemplate restTemplate;
    private final SupabaseConfig supabaseConfig;

    public OrderItemRepository(RestTemplate restTemplate, SupabaseConfig supabaseConfig) {
        this.restTemplate = restTemplate;
        this.supabaseConfig = supabaseConfig;
    }

    /*** 전체 주문 상품 조회 ***/
    public List<OrderItemDto> findAll() {
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/products?select=*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getSupabaseKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getSupabaseKey());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<OrderItemDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                OrderItemDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    /*** 주문 상품 일괄 저장 - 생성된 orderId와 함께 orderItems 테이블 INSERT ***/
    public void saveAll(List<OrderItemDto> items, Long orderId) {
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/orderItems";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getSupabaseKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getSupabaseKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        /*** return=minimal: 삽입 후 응답 본문 없음 (불필요한 데이터 전송 방지) ***/
        headers.set("Prefer", "return=minimal");

        /*** 각 상품을 orderItems 레코드로 변환 (eventType은 DB에 저장하지 않음) ***/
        List<Map<String, Object>> bodyList = new ArrayList<>();
        for (OrderItemDto item : items) {
            Map<String, Object> record = new HashMap<>();
            record.put("orderId", orderId);
            record.put("productId", item.getProductId());
            record.put("price", item.getPrice());
            record.put("quantity", item.getQuantity());
            record.put("isReceived", false);
            bodyList.add(record);
        }

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(bodyList, headers);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );
    }

    /*** 주문 ID로 주문 상품 목록 조회 ***/
    public List<OrderItemDto> findByOrderId(Long orderId) {
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/orderItems?orderId=eq." + orderId + "&select=*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getSupabaseKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getSupabaseKey());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<OrderItemDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                OrderItemDto[].class
        );

        OrderItemDto[] result = response.getBody();
        return result != null ? Arrays.asList(result) : Collections.emptyList();
    }
}

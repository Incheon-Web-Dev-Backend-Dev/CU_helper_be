package com.example.cu_helper_be.repository;

import com.example.cu_helper_be.config.SupabaseConfig;
import com.example.cu_helper_be.dto.OrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    // JDBC 대신 HTTPfh Supabase와 통신하기 위해 사용
    private final RestTemplate restTemplate;

    // supabase 설정 클래스
    // 매번 URL과 키를 하드코딩하지 않기 위해 별도 Config로 분리
    private final SupabaseConfig supabaseConfig;


    public List<OrderDto> findAll() {

        // Supabase Rest API 주소 조합
        // /rest/v1 -> Supabase REST API 기본 경로
        // /oders -> 조회할 테이븗명
        // ?select=* -> 쿼리문
        String url = supabaseConfig.getUrl() + "/rest/v1/orders?select=*";

        // HTTP요청 헤더 객체 생성 : Supabase는 인증 정보를  헤더로 받기 때문에 반드시 필요함
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getKey());

        // RLS 정책 적용 시 사용자 권한 확인용
        // "Bearer {키}" 형식은 OAuth 표준 인증 방식
        // apikey와 같은 값이지만 역할이 다름
        // apikey  → 프로젝트 식별
        // Bearer  → 사용자 권한 식별
        headers.set("Authorization", "Bearer " + supabaseConfig.getKey());

        // 응답을 JSON 형식으로 받겠다고 명시함( 없어도 동작하지만 명시적으로 선언하는 것이 안전)
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // 헤더를 HTTP 요청 객체에 담음
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<OrderDto[]> response = restTemplate.exchange(
                url, // 요청할 주소
                HttpMethod.GET, // HTTP메서드
                entity, // 헤더가 담긴 요청 객체
                OrderDto[].class
        );

        return Arrays.asList(response.getBody());
    }
}

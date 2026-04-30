package com.example.cu_helper_be.repository;

import com.example.cu_helper_be.config.SupabaseConfig;
import com.example.cu_helper_be.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final RestTemplate restTemplate;
    private final SupabaseConfig supabaseConfig;

    /*** 상품 전체 조회 ***/
    public List<ProductDto> findAll() {
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/products?select=*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getSupabaseKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getSupabaseKey());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ProductDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    // 상품 등록
    public ProductDto save(ProductDto productDto) {
        String url = supabaseConfig.getUrl() + "/rest/v1/products";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Prefer", "return=representation");
        // return=representation : 저장 후 저장된 데이터를 응답으로 반환
        // 없으면 저장 후 빈 응답이 옴

        HttpEntity<ProductDto> entity = new HttpEntity<>(productDto, headers);

        try {
            ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    ProductDto[].class
            );

            if (response.getBody() == null || response.getBody().length == 0) {
                return null;
            }

            return response.getBody()[0];
            // Supabase가 배열로 반환하므로 첫 번째 요소를 꺼내서 반환

        } catch (HttpClientErrorException e) {
            System.err.println("Product 저장 실패 - Status: " + e.getStatusCode());
            System.err.println("Response: " + e.getResponseBodyAsString());
            return null;
        }
    }
}

package com.example.cu_helper_be.repository;

import com.example.cu_helper_be.config.SupabaseConfig;
import com.example.cu_helper_be.dto.OrderItemDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class OrderItemRepository {
    private final RestTemplate restTemplate;
    private final SupabaseConfig supabaseConfig;

    public OrderItemRepository(RestTemplate restTemplate, SupabaseConfig supabaseConfig) {
        this.restTemplate = restTemplate;
        this.supabaseConfig = supabaseConfig;
    }

    public List<OrderItemDto> findAll() {
        String url = supabaseConfig.getUrl() + "/rest/v1/orderitems?select=*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<OrderItemDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                OrderItemDto[].class
        );

        return Arrays.asList(response.getBody());
    }
}

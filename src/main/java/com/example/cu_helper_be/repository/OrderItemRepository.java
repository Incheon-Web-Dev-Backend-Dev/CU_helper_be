package com.example.cu_helper_be.repository;

import com.example.cu_helper_be.config.SupabaseConfig;
import com.example.cu_helper_be.dto.OrderItemDto;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
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
}

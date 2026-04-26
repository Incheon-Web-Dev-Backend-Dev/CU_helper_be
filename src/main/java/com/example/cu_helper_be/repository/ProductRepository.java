package com.example.cu_helper_be.repository;

import com.example.cu_helper_be.config.SupabaseConfig;
import com.example.cu_helper_be.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final RestTemplate restTemplate;
    private final SupabaseConfig supabaseConfig;

    public List<ProductDto> findAll() {
        String url = supabaseConfig.getSupabaseUrl() + "/rest/v1/products?select=*";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseConfig.getSupabaseKey());
        headers.set("Authorization", "Bearer " + supabaseConfig.getSupabaseKey());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ProductDto[].class
        );

        return Arrays.asList(response.getBody());
    }
}

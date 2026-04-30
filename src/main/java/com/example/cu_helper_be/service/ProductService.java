package com.example.cu_helper_be.service;

import com.example.cu_helper_be.dto.ProductDto;
import com.example.cu_helper_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/*** 상품 서비스 - 상품 조회 및 등록 비즈니스 로직 ***/
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private  final ProductRepository productRepository;

    /*** 상품 전체 조회 ***/
    public List<ProductDto> getAllProducts() {
        log.info("getAllProducts() called");
        List<ProductDto> products = productRepository.findAll();
        log.info("getAllProducts() success - count: {}", products.size());
        return products;
    }

    // 상품 등록
    public ProductDto createProduct(ProductDto productDto) {
        return productRepository.save(productDto);
    }
}

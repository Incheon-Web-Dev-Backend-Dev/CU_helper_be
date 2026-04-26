package com.example.cu_helper_be.service;

import com.example.cu_helper_be.dto.ProductDto;
import com.example.cu_helper_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private  final ProductRepository productRepository;

    // 상품 전체 조회
    public List<ProductDto> getAllProducts() {return productRepository.findAll();}

    // 상품 등록
    public ProductDto createProduct(ProductDto productDto) {
        return productRepository.save(productDto);
    }
}

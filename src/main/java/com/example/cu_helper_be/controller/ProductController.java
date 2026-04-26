package com.example.cu_helper_be.controller;

import com.example.cu_helper_be.dto.ProductDto;
import com.example.cu_helper_be.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 전체 조회
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        // @RequestBody : 프론트에서 넘어온 JSON을 ProductDto로 자동 변환
        ProductDto saved = productService.createProduct(productDto);

        if (saved == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(saved);
    }
}

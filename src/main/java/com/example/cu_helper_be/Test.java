package com.example.cu_helper_be;

import com.example.cu_helper_be.dto.ProductDto;
import com.example.cu_helper_be.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Test implements CommandLineRunner {
    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Override
    public void run(String... args) {
        List<ProductDto> products = productRepository.findAll();
        products.forEach(System.out::println);
    }
}

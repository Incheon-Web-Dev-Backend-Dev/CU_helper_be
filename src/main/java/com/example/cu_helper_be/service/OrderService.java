package com.example.cu_helper_be.service;

import com.example.cu_helper_be.dto.OrderDto;
import com.example.cu_helper_be.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll();
    }
}

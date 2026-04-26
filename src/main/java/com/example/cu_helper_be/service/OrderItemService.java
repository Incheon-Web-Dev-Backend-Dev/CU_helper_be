package com.example.cu_helper_be.service;

import com.example.cu_helper_be.dto.OrderItemDto;
import com.example.cu_helper_be.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItemDto> getAllOrderItems() {return orderItemRepository.findAll();}
}

package com.mich.ecommerce.order.controller;

import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Integer> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(
                orderService.createdOrder(orderRequest),
                HttpStatus.CREATED
        );
    }
}

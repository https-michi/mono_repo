package com.mich.ecommerce.order.controller;

import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.dto.OrderResponse;
import com.mich.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("order-id") Integer orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }
}

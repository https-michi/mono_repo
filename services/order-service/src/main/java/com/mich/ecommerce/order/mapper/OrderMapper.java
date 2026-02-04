package com.mich.ecommerce.order.mapper;

import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.dto.OrderResponse;
import com.mich.ecommerce.order.entity.Order;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderMapper {

    public Order toOrder(OrderRequest request) {
        Objects.requireNonNull(request, "OrderRequest must not be null");
        return Order.builder()
                .id(request.id())
                .reference(request.reference())
                .totalAmount(request.amount())
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse fromOrder(Order order) {
        Objects.requireNonNull(order, "Order entity must not be null");
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}
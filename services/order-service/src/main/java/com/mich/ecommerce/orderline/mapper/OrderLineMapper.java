package com.mich.ecommerce.orderline.mapper;

import com.mich.ecommerce.order.entity.Order;
import com.mich.ecommerce.orderline.dto.OrderLineRequest;
import com.mich.ecommerce.orderline.dto.OrderLineResponse;
import com.mich.ecommerce.orderline.entity.OrderLine;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest request) {
        Objects.requireNonNull(request, "OrderLineRequest must not be null");
        return OrderLine.builder()
                .id(request.id())
//        id(request.orderId())
                .productId(request.productId())
                .order(
                        Order.builder()
                                .id(request.orderId())
                                .build()
                )
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        Objects.requireNonNull(orderLine, "OrderLine entity must not be null");
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}

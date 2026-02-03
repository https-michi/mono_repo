package com.mich.ecommerce.order.service;

import com.mich.ecommerce.customer.CustomerClient;
import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.exception.BusinessException;
import com.mich.ecommerce.product.service.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public Integer createdOrder(OrderRequest orderRequest) {
        // 1. Validar Cliente
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));
        // 2. Comprar productos (Llamada al otro MS) --> product-ms
        // 3. Persistir Orden
        // TODO: Persistir Order Lines
        // TODO: Kafka Notification
        return null;
    }
}

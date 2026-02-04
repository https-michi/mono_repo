package com.mich.ecommerce.order.service;

import com.mich.ecommerce.customer.CustomerClient;
import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.exception.BusinessException;
import com.mich.ecommerce.order.mapper.OrderMapper;
import com.mich.ecommerce.order.repository.OrderRepository;
import com.mich.ecommerce.orderline.dto.OrderLineRequest;
import com.mich.ecommerce.orderline.service.OrderLineService;
import com.mich.ecommerce.product.dto.PurchaseRequest;
import com.mich.ecommerce.product.service.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;

    public Integer createdOrder(OrderRequest orderRequest) {
        // 1. Validar Cliente
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));
        // 2. Comprar productos (Llamada al otro MS) --> product-ms
        productClient.purchaseProducts(orderRequest.products());
        var order = orderRepository.save(orderMapper.toOrder(orderRequest));
        // 3. Persistir Orden
        // TODO: Persistir Order Lines
        for (PurchaseRequest purchaseRequest : orderRequest.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }
        // TODO: Inicio de proceso de pago
        // TODO: Kafka Notification - envio de confirmacion de orden
        return null;
    }
}

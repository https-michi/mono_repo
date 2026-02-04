package com.mich.ecommerce.order.service;

import com.mich.ecommerce.customer.CustomerClient;
import com.mich.ecommerce.kafka.OrderConfirmation;
import com.mich.ecommerce.kafka.OrderProducer;
import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.dto.OrderResponse;
import com.mich.ecommerce.order.exception.BusinessException;
import com.mich.ecommerce.order.mapper.OrderMapper;
import com.mich.ecommerce.order.repository.OrderRepository;
import com.mich.ecommerce.orderline.dto.OrderLineRequest;
import com.mich.ecommerce.orderline.service.OrderLineService;
import com.mich.ecommerce.product.dto.PurchaseRequest;
import com.mich.ecommerce.product.service.ProductClient;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    @Transactional
    public Integer createdOrder(OrderRequest orderRequest) {
        // 1. validar cliente (Feign)
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists"));
        // 2. comprar productos (Feign - Sinconico)
        // aqui restamos el stock. Si no hay stock, esto lanza excepción y el transactional cancela.
        var purchaseProducts = productClient.purchaseProducts(orderRequest.products());
        // 3. guardar la orden (cabecera)
        var order = orderRepository.save(orderMapper.toOrder(orderRequest));
        // 4. guardar las lineas de la Orden - detalle
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
        // 5. TODO: Iniciar proceso de pago (payment service via Feign/Kafka)
        // 6. noti via Kafka (asincrona)
        //si falla no se envia jeje
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                orderRequest.reference(),
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                customer,
                purchaseProducts
        ));
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }
}

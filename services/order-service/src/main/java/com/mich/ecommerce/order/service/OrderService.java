package com.mich.ecommerce.order.service;

import com.mich.ecommerce.customer.CustomerClient;
import com.mich.ecommerce.kafka.OrderConfirmation;
import com.mich.ecommerce.kafka.OrderProducer;
import com.mich.ecommerce.order.dto.OrderRequest;
import com.mich.ecommerce.order.dto.OrderResponse;
import com.mich.ecommerce.exception.BusinessException;
import com.mich.ecommerce.order.mapper.OrderMapper;
import com.mich.ecommerce.order.repository.OrderRepository;
import com.mich.ecommerce.orderline.dto.OrderLineRequest;
import com.mich.ecommerce.orderline.service.OrderLineService;
import com.mich.ecommerce.payment.PaymentClient;
import com.mich.ecommerce.payment.PaymentRequest;
import com.mich.ecommerce.product.service.ProductClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    @Transactional
    public Integer createdOrder(OrderRequest orderRequest) {
        // 1. Verificación de identidad del cliente mediante comunicación sincrónica (Feign)
        var customer = customerClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> new BusinessException("No se pudo crear la orden: El cliente no existe"));

        // 2. Procesamiento de compra y reserva de stock (Operación Sincrónica)
        // Si el stock es insuficiente, el micro de productos lanzará una excepción
        // y el @Transactional ejecutará el rollback automático de toda la operación.
        var purchaseProducts = productClient.purchaseProducts(orderRequest.products());

        // 3. Persistencia de la orden (Cabecera)
        var order = orderRepository.save(orderMapper.toOrder(orderRequest));

        // 4. Registro de los detalles de la orden (Líneas de pedido)
        orderRequest.products().forEach(product ->
                orderLineService.saveOrderLine(
                        new OrderLineRequest(
                                null,
                                order.getId(),
                                product.productId(),
                                product.quantity()
                        )
                )
        );

        // 5. Gestión del pago (Comunicación Sincrónica con Payment Service)
        // Se delega la responsabilidad de cobro antes de confirmar la transacción final.
        var paymentRequest = new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);

        // 6. Publicación del evento de confirmación en el Broker (Kafka - Asíncrono)
        // El mensaje se envía solo si el flujo anterior fue exitoso. Si el envío al broker falla,
        // se revierte la orden y el pago para mantener la consistencia del sistema.
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
                orderRequest.reference(),
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                customer,
                purchaseProducts
        ));

        log.info("Orden creada exitosamente con ID: {}", order.getId());
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return orderRepository.findById(id)
                .map(orderMapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", id)));
    }
}

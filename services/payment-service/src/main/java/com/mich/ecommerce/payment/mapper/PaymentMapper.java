package com.mich.ecommerce.payment.mapper;

import com.mich.ecommerce.payment.dto.PaymentRequest;
import com.mich.ecommerce.payment.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentMapper {
    public Payment toPayment(PaymentRequest request) {
        Objects.requireNonNull(request, "Payment Request cannot be null");
        return Payment.builder()
                .id(request.id())
                .paymentMethod(request.paymentMethod())
                .amount(request.amount())
                .orderId(request.orderId())
                .build();
    }
}
package com.mich.ecommerce.payment.mapper;

import com.mich.ecommerce.notification.PaymentNotificationRequest;
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

    public PaymentNotificationRequest toNotificationRequest(PaymentRequest request) {
        return new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstname(),
                request.customer().lastname(),
                request.customer().email()
        );
    }
}
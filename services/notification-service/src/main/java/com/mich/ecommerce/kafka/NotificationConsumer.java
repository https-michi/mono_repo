package com.mich.ecommerce.kafka;

import com.mich.ecommerce.kafka.email.EmailService;
import com.mich.ecommerce.kafka.order.OrderConfirmation;
import com.mich.ecommerce.kafka.payment.NotificationRepository;
import com.mich.ecommerce.kafka.payment.PaymentConfirmation;
import com.mich.ecommerce.notification.entity.Notification;
import com.mich.ecommerce.notification.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) {
        log.info("Consuming payment success for Order: {}", paymentConfirmation.orderReference());
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );
        String customerName = String.format("%s %s", paymentConfirmation.customerFirstname(), paymentConfirmation.customerLastname());

        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(OrderConfirmation orderConfirmation) {
        log.info("Consuming order confirmation for Reference: {}", orderConfirmation.orderReference());
        notificationRepository.save(
                Notification.builder()
                        .type(NotificationType.ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );

        var customer = orderConfirmation.customer();
        String customerName = String.format("%s %s", customer.firstname(), customer.lastname());

        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}

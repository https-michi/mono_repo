package com.mich.ecommerce.payment.service;

import com.mich.ecommerce.notification.NotificationProducer;
import com.mich.ecommerce.payment.mapper.PaymentMapper;
import com.mich.ecommerce.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.mich.ecommerce.payment.dto.PaymentRequest;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;

    @Transactional
    public Integer createPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order ref: {}", paymentRequest.orderReference());
        var payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));
        notificationProducer.sendNotification(
                paymentMapper.toNotificationRequest(paymentRequest)
        );
        log.info("Payment created successfully with ID: {}", payment.getId());
        return payment.getId();
    }
}

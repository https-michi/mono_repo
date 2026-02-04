package com.mich.ecommerce.payment.service;

import com.mich.ecommerce.payment.entity.Payment;
import com.mich.ecommerce.payment.mapper.PaymentMapper;
import com.mich.ecommerce.payment.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mich.ecommerce.payment.dto.PaymentRequest;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public Integer createPayment(@Valid PaymentRequest paymentRequest) {
        var payment = paymentRepository.save(paymentMapper.toPayment(paymentRequest));
        return null;
    }
}

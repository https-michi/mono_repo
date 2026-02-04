package com.mich.ecommerce.payment.dto;

import com.mich.ecommerce.customer.Customer;
import com.mich.ecommerce.payment.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer
) {
}

package com.mich.ecommerce.payment;

import com.mich.ecommerce.customer.CustomerResponse;
import com.mich.ecommerce.order.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}

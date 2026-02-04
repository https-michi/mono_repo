package com.mich.ecommerce.kafka;

import com.mich.ecommerce.customer.CustomerResponse;
import com.mich.ecommerce.order.enums.PaymentMethod;
import com.mich.ecommerce.product.dto.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}

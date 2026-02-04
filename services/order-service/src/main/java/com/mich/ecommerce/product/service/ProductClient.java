package com.mich.ecommerce.product.service;


import com.mich.ecommerce.product.dto.PurchaseRequest;
import com.mich.ecommerce.product.dto.PurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${application.config.product-url}")
public interface ProductClient {
    @PostMapping("/purchase")
    List<PurchaseResponse> purchaseProducts(@RequestBody List<PurchaseRequest> requestBody);
}
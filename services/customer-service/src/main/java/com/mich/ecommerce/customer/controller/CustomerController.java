package com.mich.ecommerce.customer.controller;

import com.mich.ecommerce.customer.dto.CustomerRequest;
import com.mich.ecommerce.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        return ResponseEntity.ok().body(customerService.createCustomer(customerRequest));
    }

}

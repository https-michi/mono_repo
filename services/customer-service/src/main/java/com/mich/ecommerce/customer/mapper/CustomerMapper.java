package com.mich.ecommerce.customer.mapper;

import com.mich.ecommerce.customer.domain.Customer;
import com.mich.ecommerce.customer.dto.CustomerRequest;
import com.mich.ecommerce.customer.dto.CustomerResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Component
public class CustomerMapper {
    public Customer toCustomer(CustomerRequest customerRequest) {
        Objects.requireNonNull(customerRequest, "CustomerRequest must not be null");
        return Customer.builder()
                .id(customerRequest.id())
                .firstname(customerRequest.firstname())
                .lastname(customerRequest.lastname())
                .email(customerRequest.email())
                .address(customerRequest.address())
                .build();
    }

    public CustomerResponse fromCustomer(Customer customer) {
        Objects.requireNonNull(customer, "Customer must not be null");
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstname(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}

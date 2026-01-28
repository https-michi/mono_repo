package com.mich.ecommerce.customer.service;

import com.mich.ecommerce.customer.dto.CustomerRequest;
import com.mich.ecommerce.customer.mapper.CustomerMapper;
import com.mich.ecommerce.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }
}

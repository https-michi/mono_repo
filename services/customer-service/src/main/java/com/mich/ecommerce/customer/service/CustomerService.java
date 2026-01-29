package com.mich.ecommerce.customer.service;

import com.mich.ecommerce.customer.domain.Address;
import com.mich.ecommerce.customer.domain.Customer;
import com.mich.ecommerce.customer.dto.CustomerRequest;
import com.mich.ecommerce.customer.dto.CustomerResponse;
import com.mich.ecommerce.customer.exception.CustomerNotFoundException;
import com.mich.ecommerce.customer.mapper.CustomerMapper;
import com.mich.ecommerce.customer.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public String createCustomer(CustomerRequest customerRequest) {
        var customer = customerRepository.save(customerMapper.toCustomer(customerRequest));
        return customer.getId();
    }

    public void updateCustomer(CustomerRequest customerRequest) {
        Objects.requireNonNull(customerRequest, "CustomerRequest cannot be null");
        var customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(
                        String.format("Customer with ID %s not found", customerRequest.id())
                ));
        mergeCustomer(customer, customerRequest);
        customerRepository.save(customer);
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        Optional.ofNullable(request.firstname()).filter(StringUtils::isNotBlank).ifPresent(customer::setFirstname);
        Optional.ofNullable(request.lastname()).filter(StringUtils::isNotBlank).ifPresent(customer::setLastname);
        Optional.ofNullable(request.email()).filter(StringUtils::isNotBlank).map(String::toLowerCase).ifPresent(customer::setEmail);
        Optional.ofNullable(request.address()).ifPresent(addr -> mergeAddress(customer, addr));
    }

    private void mergeAddress(Customer customer, Address newAddress) {
        var current = Optional.ofNullable(customer.getAddress()).orElseGet(Address::new);
        Optional.ofNullable(newAddress.getStreet()).filter(StringUtils::isNotBlank).ifPresent(current::setStreet);
        Optional.ofNullable(newAddress.getHouseNumber()).filter(StringUtils::isNotBlank).ifPresent(current::setHouseNumber);
        Optional.ofNullable(newAddress.getZipCode()).filter(StringUtils::isNotBlank).ifPresent(current::setZipCode);

        customer.setAddress(current);
    }

    public List<CustomerResponse> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return customerRepository.findById(customerId)
                .isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with ID %s not found", customerId)));
    }

    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }
}

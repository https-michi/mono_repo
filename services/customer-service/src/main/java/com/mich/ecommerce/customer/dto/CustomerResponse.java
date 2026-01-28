package com.mich.ecommerce.customer.dto;

import com.mich.ecommerce.customer.domain.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {

}
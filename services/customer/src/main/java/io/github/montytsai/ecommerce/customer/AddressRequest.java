package io.github.montytsai.ecommerce.customer;

public record AddressRequest(
        String street,
        String city,
        String state,
        String zipCode
) {}

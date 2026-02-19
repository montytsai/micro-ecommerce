package io.github.montytsai.ecommerce.customer;

import lombok.Builder;

@Builder
public record AddressResponse(
        String street,
        String city,
        String state,
        String zipCode
) {}

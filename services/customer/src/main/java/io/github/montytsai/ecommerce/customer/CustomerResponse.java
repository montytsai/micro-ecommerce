package io.github.montytsai.ecommerce.customer;

import lombok.Builder;

@Builder
public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        AddressResponse address
) {
}

package io.github.montytsai.ecommerce.orderline;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderLineRequest(
        String id,
        Integer productId,
        BigDecimal quantity
) {
}

package io.github.montytsai.ecommerce.orderline;

import java.math.BigDecimal;

public record OrderLineResponse(
        String id,
        Integer productId,
        BigDecimal quantity
) {
}

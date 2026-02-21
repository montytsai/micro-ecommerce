package io.github.montytsai.ecommerce.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer productId,
        String name,
        String description,
        BigDecimal quantity,
        BigDecimal price,
        BigDecimal totalPrice
) {
}

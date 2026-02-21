package io.github.montytsai.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductPurchaseRequest(
        @NotNull(message = "Product ID is mandatory")
        Integer productId,

        @NotNull(message = "Purchase quantity is mandatory")
        @Positive(message = "Purchase quantity must be greater than zero")
        BigDecimal quantity
) {
}

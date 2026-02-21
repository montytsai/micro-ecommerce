package io.github.montytsai.ecommerce.order;

import java.math.BigDecimal;

public record OrderResponse(
        String id,
        String reference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String customerId
) {
}

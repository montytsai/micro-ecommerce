package io.github.montytsai.ecommerce.order;

import io.github.montytsai.ecommerce.orderline.OrderLineResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreateResponse(
        String id,
        String reference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String customerId,
        String customerEmail,
        List<OrderLineResponse> orderLines
) {
}

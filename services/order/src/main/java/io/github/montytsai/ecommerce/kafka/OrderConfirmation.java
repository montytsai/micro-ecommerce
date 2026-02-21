package io.github.montytsai.ecommerce.kafka;

import io.github.montytsai.ecommerce.customer.CustomerResponse;
import io.github.montytsai.ecommerce.order.PaymentMethod;
import io.github.montytsai.ecommerce.product.ProductPurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<ProductPurchaseResponse> products
) {
}

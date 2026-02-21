package io.github.montytsai.ecommerce.order;

import io.github.montytsai.ecommerce.product.ProductPurchaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(

        String id,

        String reference,

        @Positive(message = "Order amount should be positive")
        BigDecimal amount,

        @NotNull(message = "Payment method should be precised")
        PaymentMethod paymentMethod,

        @NotBlank(message = "Customer id should be precised")
        String customerId,

        @NotEmpty(message = "You should at least purchase one product")
        List<ProductPurchaseRequest> products

) {
}

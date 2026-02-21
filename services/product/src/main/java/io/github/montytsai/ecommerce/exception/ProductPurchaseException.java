package io.github.montytsai.ecommerce.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ProductPurchaseException extends RuntimeException {

    public ProductPurchaseException(String message) {
        super(message);
    }

}

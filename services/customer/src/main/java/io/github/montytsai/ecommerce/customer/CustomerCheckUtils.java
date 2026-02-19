package io.github.montytsai.ecommerce.customer;

import org.mapstruct.Condition;
import org.springframework.util.StringUtils;

public class CustomerCheckUtils {

    private CustomerCheckUtils() {
        throw new IllegalStateException("Utility class");
    }

    @Condition
    public static boolean isNotEmpty(String value) {
        return StringUtils.hasText(value);
    }

}
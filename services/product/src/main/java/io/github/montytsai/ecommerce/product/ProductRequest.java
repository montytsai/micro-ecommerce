package io.github.montytsai.ecommerce.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @Null(groups = ValidationGroups.Create.class, message = "ID must be null when creating a product")
        @NotNull(groups = {ValidationGroups.Update.class, ValidationGroups.Patch.class}, message = "ID is required for updating or patching a product")
        Integer id,

        @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "Product name is required")
        String name,

        @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "Product description is required")
        String description,

        @Positive(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "Available quantity should be positive")
        BigDecimal availableQuantity,

        @Positive(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "Price should be positive")
        BigDecimal price,

        @NotNull(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "Product category ID is required")
        Integer categoryId
) {}
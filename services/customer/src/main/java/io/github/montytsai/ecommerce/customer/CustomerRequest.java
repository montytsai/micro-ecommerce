package io.github.montytsai.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public record CustomerRequest(
        @Null(message = "ID must be null for creation", groups = ValidationGroups.Create.class)
        @NotBlank(message = "ID is required", groups = {ValidationGroups.Update.class, ValidationGroups.Patch.class})
        String id,

        @NotBlank(message = "First name is required", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
        String firstName,

        @NotBlank(message = "Last name is required", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
        String lastName,

        @NotBlank(message = "Email is required", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
        @Email(message = "Email should be valid", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class, ValidationGroups.Patch.class})
        String email,

        AddressRequest address
) {
}
package io.github.montytsai.ecommerce.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(service.findAllCustomers());
    }

    @RequestMapping(value = "exists/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> existsCustomerById(@PathVariable @NotBlank String id) {
        if (service.existsCustomerById(id)) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_LENGTH, "0").build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable @NotBlank String id) {
        return ResponseEntity.ok(service.findCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Validated(ValidationGroups.Create.class) CustomerRequest request) {
        var response = service.createCustomer(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable @NotBlank String id,
                                                           @RequestBody @Validated(ValidationGroups.Update.class) CustomerRequest request) {
        return ResponseEntity.ok(service.updateCustomer(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> patchCustomer(@PathVariable @NotBlank String id,
                                                          @RequestBody @Validated(ValidationGroups.Patch.class) CustomerRequest request) {
        return ResponseEntity.ok(service.patchCustomer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable @NotBlank String id) {
        service.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

}

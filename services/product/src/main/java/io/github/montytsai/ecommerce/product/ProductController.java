package io.github.montytsai.ecommerce.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(service.findAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody @Validated(ValidationGroups.Create.class) ProductRequest request) {
        var response = service.createProduct(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody @Valid @NotEmpty List<ProductPurchaseRequest> request) {
        return ResponseEntity.ok(service.purchaseProducts(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @RequestBody @Validated(ValidationGroups.Update.class) ProductRequest request) {
        return ResponseEntity.ok(service.updateProduct(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> patchProduct(
            @PathVariable Integer id,
            @RequestBody @Validated(ValidationGroups.Patch.class) ProductRequest request) {
        return ResponseEntity.ok(service.patchProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Integer id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}

package io.github.montytsai.ecommerce.orderline;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-line")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService service;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> getAllOrderLineByOrderId(
            @PathVariable("order-id") @NotBlank String orderId) {
        return ResponseEntity.ok(service.findAllOrderLineByOrderId(orderId));
    }

}

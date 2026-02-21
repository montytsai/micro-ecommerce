package io.github.montytsai.ecommerce.order;

import com.github.f4b6a3.ulid.UlidCreator;
import io.github.montytsai.ecommerce.customer.CustomerClient;
import io.github.montytsai.ecommerce.exception.BusinessException;
import io.github.montytsai.ecommerce.kafka.OrderConfirmation;
import io.github.montytsai.ecommerce.kafka.OrderProducer;
import io.github.montytsai.ecommerce.orderline.OrderLineRequest;
import io.github.montytsai.ecommerce.orderline.OrderLineResponse;
import io.github.montytsai.ecommerce.orderline.OrderLineService;
import io.github.montytsai.ecommerce.product.ProductClient;
import io.github.montytsai.ecommerce.product.ProductPurchaseRequest;
import io.github.montytsai.ecommerce.product.ProductPurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    private final CustomerClient customerClient;
    private final ProductClient productClient;

    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return repository.findAll().stream()
                .map(mapper::fromOrder)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrderById(String orderId) {
        return repository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order not found with ID: %s", orderId)));
    }

    @Transactional
    public OrderCreateResponse createOrder(OrderRequest request) {
        log.info("Creating order with request: {}", request);

        // 1. check the customer exists
        var customer = customerClient.getCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException(String.format("Customer not found with ID: %s", request.customerId())));

        // 2. persist the order and order lines
        Order order = mapper.toOrder(request);
        order.setId(UlidCreator.getMonotonicUlid().toString());
        var savedOrder = repository.save(order);

        List<OrderLineResponse> orderLines = new ArrayList<>();
        for (ProductPurchaseRequest purchaseRequest : request.products()) {
            OrderLineRequest orderLineRequest = OrderLineRequest.builder()
                    .productId(purchaseRequest.productId())
                    .quantity(purchaseRequest.quantity())
                    .build();
            OrderLineResponse orderLineResponse = orderLineService.saveOrderLine(orderLineRequest, savedOrder);
            orderLines.add(orderLineResponse);
        }

        // 3. check the products exist and have enough stock
        List<ProductPurchaseResponse> products;
        try {
            products = productClient.purchaseProducts(request.products());
        } catch (Exception e) {
            throw new BusinessException("An error occurred while processing the products purchase: " + e.getMessage());
        }

        // 4. TODO process payment

        // 5. send order confirmation email
        orderProducer.sendOrderMessage(new OrderConfirmation(
                request.reference(),
                request.amount(),
                request.paymentMethod(),
                customer,
                products
        ));

        return mapper.toOrderCreateResponse(savedOrder, customer, orderLines);
    }

}

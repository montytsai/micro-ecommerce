package io.github.montytsai.ecommerce.orderline;

import com.github.f4b6a3.ulid.UlidCreator;
import io.github.montytsai.ecommerce.order.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public OrderLineResponse saveOrderLine(OrderLineRequest request, Order order) {
        log.info("Saving order line with request: {}", request);
        var orderLine = mapper.toOrderLine(request);
        orderLine.setId(UlidCreator.getMonotonicUlid().toString());
        orderLine.setOrder(order);
        return mapper.fromOrderLine(repository.save(orderLine));
    }

    public List<OrderLineResponse> findAllOrderLineByOrderId(String orderId) {
        return repository.findAllByOrderId(orderId).stream()
                .map(mapper::fromOrderLine)
                .toList();
    }

}

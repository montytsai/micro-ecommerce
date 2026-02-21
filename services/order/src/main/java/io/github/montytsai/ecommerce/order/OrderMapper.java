package io.github.montytsai.ecommerce.order;

import io.github.montytsai.ecommerce.customer.CustomerResponse;
import io.github.montytsai.ecommerce.orderline.OrderLineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalAmount", source = "request.amount")
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toOrder(OrderRequest request);

    OrderResponse fromOrder(Order order);

    @Mapping(target = "id", source = "order.id")
    @Mapping(target = "customerEmail", source = "customer.email")
    @Mapping(target = "orderLines", source = "orderLines")
    OrderCreateResponse toOrderCreateResponse(Order order, CustomerResponse customer, List<OrderLineResponse> orderLines);

}

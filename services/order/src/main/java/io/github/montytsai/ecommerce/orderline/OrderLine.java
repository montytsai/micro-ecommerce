package io.github.montytsai.ecommerce.orderline;

import io.github.montytsai.ecommerce.common.BaseEntity;
import io.github.montytsai.ecommerce.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "order_line")
public class OrderLine extends BaseEntity {

    @Id
    @Column(length = 26, nullable = false, updatable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order"))
    private Order order;

    private Integer productId;

    private BigDecimal quantity;

}
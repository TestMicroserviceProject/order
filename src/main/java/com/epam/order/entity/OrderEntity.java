package com.epam.order.entity;

import com.epam.order.dto.OrderDto;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
  @SequenceGenerator(name = "orders_seq", sequenceName = "orders_order_entity_id_seq", allocationSize = 1)
  @Column(name = "order_entity_id")
  private Long id;
  private Long orderId;
  private Long customerId;
  @OneToMany(mappedBy = "orderEntity")
  private List<ItemEntity> items;
  private String location;
  private Double total;
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public OrderEntity(OrderDto dto) {
    this.customerId = dto.getCustomerId();
    this.orderId = dto.getOrderId();
    this.items = ItemEntity.convert(dto.getItemDtos());
    this.location = dto.getLocation();
    this.total = dto.getTotal();
    this.status = OrderStatus.ON_CHECK;
  }

  public enum OrderStatus {
    ON_CHECK, SUCCESS, FAIL
  }
}

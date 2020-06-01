package com.epam.order.entity;

import com.epam.order.dto.OrderDto;
import com.epam.order.dto.ResultDto.Check;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sagas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SagaEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sagas_seq")
  @SequenceGenerator(name = "sagas_seq", sequenceName = "sagas_id_seq", allocationSize = 1)
  private Long id;
  private Long customerId;
  private Long orderId;
  @Enumerated(EnumType.STRING)
  private State currentState;
  @Enumerated(EnumType.STRING)
  private Check paymentResult;
  @Enumerated(EnumType.STRING)
  private Check stockResult;
  @Enumerated(EnumType.STRING)
  private Check deliveryResult;

  public SagaEntity(OrderDto orderDto) {
    this.customerId = orderDto.getCustomerId();
    this.orderId = orderDto.getOrderId();
    this.currentState = State.STARTED;
  }

  public enum State {
    STARTED,
    ROLLBACK,
    DONE
  }

}

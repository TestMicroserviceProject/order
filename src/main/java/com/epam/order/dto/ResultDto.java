package com.epam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {

  private Long orderId;
  private Long customerId;
  private Check check;
  private Service serviceName;

  public ResultDto(OrderDto order, Check check, Service serviceName) {
    this.orderId = order.getOrderId();
    this.customerId = order.getCustomerId();
    this.check = check;
    this.serviceName = serviceName;
  }

  public enum Check {
    SUCCESS, FAIL, CANCELLED
  }

  public enum Service {
    DELIVERY, STOCK, PAYMENT
  }
}

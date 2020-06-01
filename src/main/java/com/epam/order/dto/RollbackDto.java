package com.epam.order.dto;

import lombok.Data;

@Data
public class RollbackDto {

  private final Long clientId;
  private final Long orderId;

  public RollbackDto(ResultDto resultDto) {
    this.clientId = resultDto.getCustomerId();
    this.orderId = resultDto.getOrderId();
  }
}

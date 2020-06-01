package com.epam.order.service;

import com.epam.order.dto.OrderDto;
import com.epam.order.dto.ResultDto;

public interface OrderService {

  String makeOrder(OrderDto orderDto);

  String checkOrder(Long customerId, Long orderId);

  void rollback(ResultDto resultDto);
}

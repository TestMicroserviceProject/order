package com.epam.order.controller;

import com.epam.order.dto.OrderDto;
import com.epam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService service;

  @PostMapping("/order")
  public String makeOrder(@RequestBody OrderDto order) {
    return service.makeOrder(order);
  }

  @GetMapping("/order/{clientId}/{orderId}")
  public String checkOrder(@PathVariable Long clientId, @PathVariable Long orderId) {return service.checkOrder(clientId, orderId);}
}

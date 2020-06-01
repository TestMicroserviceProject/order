package com.epam.order.service;

import com.epam.order.dto.OrderDto;
import com.epam.order.dto.ResultDto;
import com.epam.order.dto.ResultDto.Check;
import com.epam.order.dto.RollbackDto;
import com.epam.order.entity.OrderEntity;
import com.epam.order.entity.SagaEntity;
import com.epam.order.kafka.KafkaProducer;
import com.epam.order.repository.OrderRepository;
import com.epam.order.repository.SagaRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private static final String WAIT_RESPONSE = "Your order with number %s was registered";
  private static final String SUCCESS_RESPONSE = "Your order was created successfully";
  private static final String FAIL_RESPONSE = "Unable to create order: %s";

  private final OrderRepository orderRepository;
  private final SagaRepository sagaRepository;
  private final KafkaProducer kafkaProducer;

  @Override
  @SneakyThrows
  @Transactional
  public String makeOrder(OrderDto orderDto) {
    OrderEntity orderEntity = new OrderEntity(orderDto);
    orderEntity = orderRepository.save(orderEntity);
    SagaEntity sagaEntity = new SagaEntity(orderDto);
    sagaEntity = sagaRepository.save(sagaEntity);
    kafkaProducer.send(orderDto);
    return String.format(WAIT_RESPONSE, orderDto.getOrderId());
  }

  @Override
  @Transactional
  public String checkOrder(Long customerId, Long orderId) {
    final SagaEntity saga = sagaRepository
        .findByCustomerIdAndOrderId(customerId, orderId);
    final Check paymentResult = saga.getPaymentResult();
    final Check stockResult = saga.getStockResult();
    final Check deliveryResult = saga.getDeliveryResult();

    if (Check.FAIL.equals(paymentResult) ||
        Check.FAIL.equals(stockResult) ||
        Check.FAIL.equals(deliveryResult)
    ) {
      String reason = "";
      if (Check.FAIL.equals(paymentResult)) {
        reason = reason.concat("Not enough money ");
      }
      if (Check.FAIL.equals(stockResult)) {
        reason = reason.concat("Not enough items in stock ");
      }
      if (Check.FAIL.equals(deliveryResult)) {
        reason = reason.concat("Can't deliver to this place ");
      }
      return String.format(FAIL_RESPONSE, reason);
    }
    if (Check.SUCCESS.equals(paymentResult) &&
        Check.SUCCESS.equals(stockResult) &&
        Check.SUCCESS.equals(deliveryResult)) {
      return SUCCESS_RESPONSE;
    }
    return "Your order processing";
  }

  @Override
  public void rollback(ResultDto resultDto) {
    final RollbackDto rollbackDto = new RollbackDto(resultDto);
    kafkaProducer.rollback(rollbackDto);
  }
}

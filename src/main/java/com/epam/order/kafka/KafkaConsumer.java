package com.epam.order.kafka;

import com.epam.order.dto.ResultDto;
import com.epam.order.dto.ResultDto.Check;
import com.epam.order.dto.ResultDto.Service;
import com.epam.order.entity.OrderEntity;
import com.epam.order.entity.OrderEntity.OrderStatus;
import com.epam.order.entity.SagaEntity;
import com.epam.order.repository.OrderRepository;
import com.epam.order.repository.SagaRepository;
import com.epam.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ObjectMapper objectMapper;
  private final SagaRepository sagaRepository;
  private final OrderService orderService;
  private final OrderRepository orderRepository;

  @SneakyThrows
  @KafkaListener(
      topics = {
          "${spring.kafka.consumer.payment.topic}",
          "${spring.kafka.consumer.stock.topic}",
          "${spring.kafka.consumer.delivery.topic}"},
      clientIdPrefix = "#{T(java.util.UUID).randomUUID().toString()}",
      groupId = "#{T(java.util.UUID).randomUUID().toString()}",
      containerFactory = "consumerFactory"
  )
  public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
    final ResultDto resultDto = objectMapper.readValue(record.value(), ResultDto.class);
    final SagaEntity saga = getSaga(resultDto);
    final Check check = setSagaStatus(resultDto, saga);
    final OrderStatus orderStatus = setOrderStatus(saga);
    if (Check.FAIL.equals(check)) {
      orderService.rollback(resultDto);
    }
    acknowledgment.acknowledge();
  }

  private SagaEntity getSaga(ResultDto resultDto) {
    final Long customerId = resultDto.getCustomerId();
    final Long orderId = resultDto.getOrderId();
    return sagaRepository.findByCustomerIdAndOrderId(customerId, orderId);
  }

  private Check setSagaStatus(ResultDto resultDto, SagaEntity saga) {
    final Check check = resultDto.getCheck();
    final Service serviceName = resultDto.getServiceName();
    if (serviceName.equals(Service.PAYMENT)) {
      saga.setPaymentResult(check);
    }
    if (serviceName.equals(Service.STOCK)) {
      saga.setStockResult(check);
    }
    if (serviceName.equals(Service.DELIVERY)) {
      saga.setDeliveryResult(check);
    }
    sagaRepository.save(saga);
    return check;
  }

  private OrderStatus setOrderStatus(SagaEntity saga) {
    final Check paymentResult = saga.getPaymentResult();
    final Check stockResult = saga.getStockResult();
    final Check deliveryResult = saga.getDeliveryResult();
    final OrderEntity order = orderRepository
        .findByCustomerIdAndOrderId(saga.getCustomerId(), saga.getOrderId());
    if (Check.SUCCESS.equals(paymentResult) &&
        Check.SUCCESS.equals(stockResult) &&
        Check.SUCCESS.equals(deliveryResult)) {
      order.setStatus(OrderStatus.SUCCESS);
      orderRepository.save(order);
    }
    if (Check.FAIL.equals(paymentResult) ||
        Check.FAIL.equals(stockResult) ||
        Check.FAIL.equals(deliveryResult)) {
      order.setStatus(OrderStatus.FAIL);
      orderRepository.save(order);
    }

    return order.getStatus();
  }
}

package com.epam.order.kafka;

import com.epam.order.dto.OrderDto;
import com.epam.order.dto.RollbackDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

  private final String paymentTopic;
  private final String stockTopic;
  private final String deliveryTopic;
  private final String paymentRollback;
  private final String stockRollback;
  private final String deliveryRollback;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Autowired
  public KafkaProducer(
      @Value("${spring.kafka.producer.payment.topic}") String paymentTopic,
      @Value("${spring.kafka.producer.stock.topic}") String stockTopic,
      @Value("${spring.kafka.producer.delivery.topic}") String deliveryTopic,
      @Value("${spring.kafka.producer.payment.rollback.topic}") String paymentRollback,
      @Value("${spring.kafka.producer.stock.rollback.topic}") String stockRollback,
      @Value("${spring.kafka.producer.delivery.rollback.topic}") String deliveryRollback,
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    this.paymentTopic = paymentTopic;
    this.stockTopic = stockTopic;
    this.deliveryTopic = deliveryTopic;
    this.paymentRollback = paymentRollback;
    this.stockRollback = stockRollback;
    this.deliveryRollback = deliveryRollback;
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  @SneakyThrows
  public void send(OrderDto order) {
    final String value = objectMapper.writeValueAsString(order);
    kafkaTemplate.send(paymentTopic, value);
    kafkaTemplate.send(stockTopic, value);
    kafkaTemplate.send(deliveryTopic, value);
  }

  @SneakyThrows
  public void rollback(RollbackDto rollbackDto) {
    final String value = objectMapper.writeValueAsString(rollbackDto);
    kafkaTemplate.send(paymentRollback, value);
    kafkaTemplate.send(stockRollback, value);
    kafkaTemplate.send(deliveryRollback, value);
  }
}

package com.epam.order.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {

 private final KafkaProperties kafkaProperties;

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> consumerFactory() {
    var properties = kafkaProperties.buildConsumerProperties();
    var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(properties);

    var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
    factory.setConsumerFactory(consumerFactory);

    return factory;
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    var properties = kafkaProperties.buildProducerProperties();
    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}

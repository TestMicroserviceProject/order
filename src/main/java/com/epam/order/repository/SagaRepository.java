package com.epam.order.repository;

import com.epam.order.entity.SagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SagaRepository extends JpaRepository<SagaEntity, Long> {

  SagaEntity findByCustomerIdAndOrderId(Long customerId, Long orderId);

}

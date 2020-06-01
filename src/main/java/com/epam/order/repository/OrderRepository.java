package com.epam.order.repository;

import com.epam.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  OrderEntity findByCustomerIdAndOrderId(long customerId, long orderId);

}

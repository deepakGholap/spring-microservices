package com.microservicelearning.orderservice.repository;

import com.microservicelearning.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderServiceRepository extends JpaRepository<Order, Long> {
}

package io.turntabl.super2.orderProcessingService.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDTO, Long> {}

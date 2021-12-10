package io.turntabl.super_two.order_processing_service.order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDTO, Long> {}

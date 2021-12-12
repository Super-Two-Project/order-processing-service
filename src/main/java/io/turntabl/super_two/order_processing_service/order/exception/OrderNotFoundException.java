package io.turntabl.super_two.order_processing_service.order.exception;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(Long id) {
        super(String.format("Order with ID: %s does not exist", id));
    }
}
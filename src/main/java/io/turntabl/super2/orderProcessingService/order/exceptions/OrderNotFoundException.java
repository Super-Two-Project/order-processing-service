package io.turntabl.super2.orderProcessingService.order.exceptions;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(Long id) {
        super(String.format("Order with ID: %s does not exist", id));
    }
}

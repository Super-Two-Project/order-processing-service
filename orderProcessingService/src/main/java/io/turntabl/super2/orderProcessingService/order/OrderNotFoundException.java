package io.turntabl.super2.orderProcessingService.order;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super(String.format("Order with ID: %s does not exist", id));
    }
}

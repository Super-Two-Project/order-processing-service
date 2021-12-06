package io.turntabl.super2.orderProcessingService.order.exceptions;

public class InvalidOrderException extends RuntimeException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
}
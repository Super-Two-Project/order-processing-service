package io.turntabl.super_two.order_processing_service.order.exceptions;

public class InvalidOrderException extends RuntimeException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
}
package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;

public class Order {

    private final String product;
    private final Double quantity;
    private final Double price;
    private final String side;

    public Order(OrderRequest orderRequest) {
        this.quantity = orderRequest.getQuantity();
        this.price = orderRequest.getPrice();
        this.side = orderRequest.getSide().name();
        this.product = "MSFT";
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public Double getPrice() {
        return this.price;
    }

    public String getSide() {
        return this.side;
    }

    public String getProduct() {
        return this.product;
    }
}

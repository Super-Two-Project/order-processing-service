package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;

public class Order {

    private String product;
    private Double quantity;
    private Double price;
    private String side;

    public Order(OrderRequest orderRequest) {
        this.quantity = orderRequest.getQuantity();
        this.price = orderRequest.getPrice();
        this.side = orderRequest.getSide().name();
        this.product = "MSFT";
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}

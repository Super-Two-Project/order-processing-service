package io.turntabl.super2.orderProcessingService.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.turntabl.super2.orderProcessingService.enums.Side;

public class Order {

    private String product;
    private Double quantity;
    private Double price;
    private Side side;

    public Order() {}

    public Order(OrderRequest orderRequest) {
        this.quantity = orderRequest.getQuantity();
        this.price = orderRequest.getPrice();
        this.side = orderRequest.getSide();
        this.product = orderRequest.getProduct();
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

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "Order{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side=" + side +
                '}';
    }
}

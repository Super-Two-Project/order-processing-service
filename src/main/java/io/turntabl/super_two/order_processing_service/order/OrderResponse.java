package io.turntabl.super_two.order_processing_service.order;

import io.turntabl.super_two.order_processing_service.enums.Side;

import java.time.LocalDate;

public class OrderResponse {

    private final Long id;
    private final Double quantity;
    private final Double price;
    private final Side side;
    private final int accountID;
    private final int productID;
    private final int portfolioID;
    private final LocalDate createdAt;
    private final LocalDate modifiedAt;

    public OrderResponse(OrderDTO orderDTO) {
        this.id = orderDTO.getId();
        this.quantity = orderDTO.getQuantity();
        this.price = orderDTO.getPrice();
        this.side = orderDTO.getSide();
        this.accountID = orderDTO.getAccountID();
        this.productID = orderDTO.getProductID();
        this.portfolioID = orderDTO.getPortfolioID();
        this.createdAt = orderDTO.getCreatedAt();
        this.modifiedAt = orderDTO.getModifiedAt();
    }

    public Long getId() {
        return this.id;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public Double getPrice() {
        return this.price;
    }

    public Side getSide() {
        return this.side;
    }

    public int getAccountID() {
        return this.accountID;
    }

    public int getProductID() {
        return this.productID;
    }

    public int getPortfolioID() {
        return this.portfolioID;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getModifiedAt() {
        return modifiedAt;
    }
}

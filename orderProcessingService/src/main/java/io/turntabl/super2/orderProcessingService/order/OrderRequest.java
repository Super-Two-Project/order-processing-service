package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;

public class OrderRequest {

    private final int productID;
    private final Double quantity;
    private final Double price;
    private final Side side;
    private final int accountID;
    private final int portfolioID;

    public OrderRequest(Double quantity, Double price, Side side, int accountID, int productID, int portfolioID) {
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.accountID = accountID;
        this.productID = productID;
        this.portfolioID = portfolioID;
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
        return productID;
    }

    public int getPortfolioID() {
        return portfolioID;
    }
}

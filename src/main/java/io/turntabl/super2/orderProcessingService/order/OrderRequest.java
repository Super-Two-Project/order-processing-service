package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;

public class OrderRequest {

    private final String product;
    private final Double quantity;
    private final Double price;
    private final Side side;
    private final int accountID;
    private final int portfolioID;
    private final String exchange;
    private final int buyLimit;

    public OrderRequest(Double quantity, Double price, Side side, int accountID, String ticker, int portfolioID, String exchange, int buyLimit) {
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.accountID = accountID;
        this.product = ticker;
        this.portfolioID = portfolioID;
        this.exchange = exchange;
        this.buyLimit = buyLimit;
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

    public String getProduct() {
        return product;
    }

    public int getPortfolioID() {
        return portfolioID;
    }

    public String getExchange() {
        return this.exchange;
    }

    public int getBuyLimit() {
        return this.buyLimit;
    }
}

package io.turntabl.super_two.order_processing_service.market_data;

public class MarketQuote {

    private String exchange;
    private Double price;
    private int limit;

    public MarketQuote(String exchange, Double price, int limit) {
        this.exchange = exchange;
        this.price = price;
        this.limit = limit;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}

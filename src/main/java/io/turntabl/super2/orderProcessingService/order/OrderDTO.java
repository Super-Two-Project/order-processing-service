package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "order_table")
public class OrderDTO {

    private @Id @GeneratedValue Long id;
    private int productID;
    private Double quantity;
    private Double price;
    private Side side;
    private int accountID;
    private int portfolioID;
    private LocalDate createdAt;
    private LocalDate modifiedAt;

    public OrderDTO() {}

    public OrderDTO(Double quantity, Double price, Side side, int accountID, int productID, int portfolioID) {
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.accountID = accountID;
        this.productID = productID;
        this.portfolioID = portfolioID;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Side getSide() {
        return this.side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public int getAccountID() {
        return this.accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getProductID() {
        return this.productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getPortfolioID() {
        return this.portfolioID;
    }

    public void setPortfolioID(int portfolioID) {
        this.portfolioID = portfolioID;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productID=" + productID +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side=" + side +
                ", accountID=" + accountID +
                ", portfolioID=" + portfolioID +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
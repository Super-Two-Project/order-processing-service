package io.turntabl.super_two.order_processing_service.order;

import io.turntabl.super_two.order_processing_service.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private Double quantity;
    private Double price;
    private Side side;
    private int accountID;
    private int productID;
    private int portfolioID;
    private LocalDate createdAt;
    private LocalDate modifiedAt;

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
}

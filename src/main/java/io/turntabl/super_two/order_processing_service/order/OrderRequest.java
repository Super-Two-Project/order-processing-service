package io.turntabl.super_two.order_processing_service.order;

import io.turntabl.super_two.order_processing_service.enums.Side;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String product;
    private Double quantity;
    private Double price;
    private Side side;
    private int accountID;
    private int portfolioID;
    private String exchange;
    private int buyLimit;
}

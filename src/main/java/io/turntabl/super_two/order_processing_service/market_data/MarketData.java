package io.turntabl.super_two.order_processing_service.market_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketData {

    private Double lastTradedPrice;
    private int sellLimit;
    private Double bidPrice;
    private Double askPrice;
    private int buyLimit;
    private String ticker;
    private int maxPriceShift;
}
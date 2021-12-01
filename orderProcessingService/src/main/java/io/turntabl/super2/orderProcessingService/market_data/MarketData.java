package io.turntabl.super2.orderProcessingService.market_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketData {

    private int lastTradedPrice;
    private int sellLimit;
    private int bidLimit;
    private int askPrice;
    private int buyLimit;
    private String ticker;
    private int maxPriceShift;
}

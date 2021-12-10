package io.turntabl.super_two.order_processing_service.market_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@RedisHash("ExchangeMarketData")
public class ExchangeMarketData implements Serializable {

    private String id;
    private List<MarketData> marketData;
}

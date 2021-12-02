package io.turntabl.super2.orderProcessingService.market_data;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

public class MarketDataController {

    private final ReactiveRedisOperations<String, MarketData> marketDataOps;

    MarketDataController(ReactiveRedisOperations<String, MarketData> coffeeOps) {
        this.marketDataOps = coffeeOps;
    }

    @GetMapping("/market_data")
    public Flux<MarketData> all() {
        return marketDataOps.keys("*")
                .flatMap(marketDataOps.opsForValue()::get);
    }
}

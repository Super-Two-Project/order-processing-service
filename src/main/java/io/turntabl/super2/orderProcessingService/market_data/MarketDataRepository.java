package io.turntabl.super2.orderProcessingService.market_data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepository extends CrudRepository<ExchangeMarketData, String> {}

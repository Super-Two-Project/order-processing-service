package io.turntabl.super_two.order_processing_service.market_data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDataRepository extends CrudRepository<ExchangeMarketData, String> {}

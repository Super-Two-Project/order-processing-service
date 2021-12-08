package io.turntabl.super2.orderProcessingService.market_data;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class MarketDataService {
//
//    Map<String, List<MarketData>> md = new HashMap<>();
//
//    @Autowired
//    MarketDataRepository marketDataRepository;
//
//    public void getExchangeMarketData() {
//        ExchangeMarketData ex1 = this.marketDataRepository.findById("exchange1")
//                .orElseThrow(RuntimeException::new);
//
//        ExchangeMarketData ex2 = this.marketDataRepository.findById("exchange2")
//                .orElseThrow(RuntimeException::new);
//
//        this.md.put(ex1.getId(), ex1.getMarketData());
//        this.md.put(ex2.getId(), ex2.getMarketData());
//    }
//}

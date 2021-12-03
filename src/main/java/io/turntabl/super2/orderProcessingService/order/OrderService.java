package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;
import io.turntabl.super2.orderProcessingService.market_data.MarketQuote;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    public MarketQuote getPrice(String ticker, Side side);
    public List<OrderResponse> getOrders();
    public ResponseEntity<?> createOrder(OrderRequest orderRequest);
    public ResponseEntity<OrderResponse> getOrder(Long id);
    public ResponseEntity<OrderResponse> updateOrder(OrderRequest orderRequest, Long id);
    public void deleteOrder(Long id);
}

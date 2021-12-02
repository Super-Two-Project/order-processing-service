package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface OrderService {

    public Map.Entry<String, Double> getPrice(String ticker, Side side);
    public List<OrderResponse> getOrders();
    public ResponseEntity<?> createOrder(OrderRequest orderRequest);
    public ResponseEntity<OrderResponse> getOrder(Long id);
    public ResponseEntity<OrderResponse> updateOrder(OrderRequest orderRequest, Long id);
    public void deleteOrder(Long id);
}

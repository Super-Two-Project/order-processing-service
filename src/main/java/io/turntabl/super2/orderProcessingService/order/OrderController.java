package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;
import io.turntabl.super2.orderProcessingService.market_data.MarketQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/orders/best-price", produces = "application/json")
    public ResponseEntity<MarketQuote> getBestPrice(@RequestParam String ticker, @RequestParam Side side) {
        return new ResponseEntity<>(this.orderService.getPrice(ticker, side), HttpStatus.OK);
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return this.orderService.getOrders();
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        return this.orderService.createOrder(orderRequest);
    }

    // Single item
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return this.orderService.getOrder(id);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody OrderRequest orderRequest, @PathVariable Long id) {
        return this.orderService.updateOrder(orderRequest, id);
    }

    @DeleteMapping("/orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        this.orderService.deleteOrder(id);
    }
}

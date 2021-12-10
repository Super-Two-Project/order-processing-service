package io.turntabl.super_two.order_processing_service.order;

import io.turntabl.super_two.order_processing_service.enums.Side;
import io.turntabl.super_two.order_processing_service.market_data.MarketQuote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/orders/best-price", produces = "application/json")
    public ResponseEntity<MarketQuote> getBestPrice(@RequestParam String ticker, @RequestParam Side side) {
        return new ResponseEntity<>(this.orderService.getPrice(ticker, side), HttpStatus.OK);
    }

    @GetMapping("/emitter")
    public SseEmitter eventEmitter() {
//        SseEmitter emitter = new SseEmitter(12000L); //12000 here is the timeout and it is optional
        SseEmitter emitter = new SseEmitter();

        //create a single thread for sending messages asynchronously
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            try {
                while (true) {
                    emitter.send(this.orderService.getPrice("IBM", Side.SELL));
                    Thread.sleep(5000);
                }
            } catch(Exception e) {
                emitter.completeWithError(e);
            } finally {
                emitter.complete();
            }
        });
        executor.shutdown();
        return emitter;
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

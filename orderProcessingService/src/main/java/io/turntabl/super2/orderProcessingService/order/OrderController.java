package io.turntabl.super2.orderProcessingService.order;

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

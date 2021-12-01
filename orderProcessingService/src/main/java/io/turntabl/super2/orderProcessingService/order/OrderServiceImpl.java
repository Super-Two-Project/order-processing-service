package io.turntabl.super2.orderProcessingService.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${app.data.exchange.url}")
    private String exchange;

    @Value("${app.data.exchange2.url}")
    private String exchange2;

    @Value("${app.data.apikey}")
    private String API_KEY;

    @Autowired
    RestTemplate restTemplate;

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderResponse> getOrders() {
        return this.orderRepository.findAll().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> createOrder(OrderRequest orderRequest) {
        // Create empty order object
        OrderDTO orderDTO = new OrderDTO();

        // Set attributes on order object
        orderDTO.setQuantity(orderRequest.getQuantity());
        orderDTO.setPrice(orderRequest.getPrice());
        orderDTO.setSide(orderRequest.getSide());
        orderDTO.setAccountID(orderRequest.getAccountID());

        Order order = new Order(orderRequest);


        // Send order to exchange
//        ResponseEntity<String> response =webClient
//                .post()
//                .uri(this.exchange + "/" + this.API_KEY + "/order")
//                .body(Mono.just(order), Order.class)
//                .retrieve()
//                .toEntity(String.class)
//                .block();

//        if (!response.getStatusCode().is2xxSuccessful()){
//            System.out.println(response.getBody());
//        }

        return restTemplate.postForEntity(this.exchange + "/" + this.API_KEY + "/order", order, String.class);

//        OrderDTO returnedOrderDTO = this.orderRepository.save(orderDTO);
//        return new ResponseEntity<>(new OrderResponse(returnedOrderDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(Long id) {
        return this.orderRepository.findById(id)
                .map(orderDTO -> new ResponseEntity<>(new OrderResponse(orderDTO), HttpStatus.OK))
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrder(OrderRequest orderRequest, Long id) {
        OrderDTO updatedOrderDTO = this.orderRepository.findById(id)
                .map(orderDTO -> {
                    orderDTO.setQuantity(orderRequest.getQuantity());
                    orderDTO.setPrice(orderRequest.getPrice());
                    orderDTO.setSide(orderRequest.getSide());
                    return this.orderRepository.save(orderDTO);
                })
                .orElseGet(() -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setId(id);
                    return this.orderRepository.save(orderDTO);
                });

        return new ResponseEntity<>(new OrderResponse(updatedOrderDTO), HttpStatus.OK);
    }

    @Override
    public void deleteOrder(Long id) {
        this.orderRepository.deleteById(id);
    }
}

package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;
import io.turntabl.super2.orderProcessingService.market_data.MarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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

    Map<String, Map<String, List<MarketData>>> md = new HashMap<>();

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Map.Entry<String, Double> getPrice(String ticker, Side side) {

        Map<String, Double> tempContainer = new HashMap<>();
        md.put(
                "exchange1", Map.of(
                        "MSFT", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "MSFT", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "MSFT", 1),
                                new MarketData(3.0, 2, 19.0, 14.1, 1, "MSFT", 1)
                        ),
                        "AAPL", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "AAPL", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "AAPL", 1),
                                new MarketData(3.0, 2, 19.9, 14.0, 1, "AAPL", 1)
                        )
                )
        );



        md.put(
                "exchange2", Map.of(
                        "AAPL", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "AAPL", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "AAPL", 1),
                                new MarketData(3.0, 2, 19.0, 14.0, 1, "AAPL", 1)
                        ),
                        "MSFT", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "MSFT", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "MSFT", 1),
                                new MarketData(3.0, 2, 19.1, 14.0, 1, "MSFT", 1)
                        )
                )
        );

        // Get keys of datastore
        var keys = md.keySet();

        // Loop through keys
        for (String key: keys) {
            Double potentialBestPrice;
            if (side.equals(Side.SELL)) {
                potentialBestPrice = md.get(key).get(ticker).stream()
                        .map(MarketData::getBidPrice)
                        .max(Comparator.comparingDouble(p -> p))
                        .orElseThrow(RuntimeException::new);
            } else {
                potentialBestPrice = md.get(key).get(ticker).stream()
                        .map(MarketData::getAskPrice)
                        .max(Comparator.comparingDouble(p -> p))
                        .orElseThrow(RuntimeException::new);
            }
            tempContainer.put(key, potentialBestPrice);
        }

        return tempContainer.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElseThrow(RuntimeException::new);
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

        md.put(
                "exchange1", Map.of(
                        "MSFT", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "MSFT", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "MSFT", 1),
                                new MarketData(3.0, 2, 19.0, 14.0, 1, "MSFT", 1)
                        ),
                        "AAPL", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "AAPL", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "AAPL", 1),
                                new MarketData(3.0, 2, 19.0, 14.0, 1, "AAPL", 1)
                        )
                )
        );

        md.put(
                "exchange2", Map.of(
                        "AAPL", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "AAPL", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "AAPL", 1),
                                new MarketData(3.0, 2, 19.0, 14.0, 1, "AAPL", 1)
                        ),
                        "MSFT", List.of(
                                new MarketData(1.0, 3, 16.0, 11.0, 11, "MSFT", 1),
                                new MarketData(2.0, 1, 1.0, 10.0, 13, "MSFT", 1),
                                new MarketData(3.0, 2, 19.0, 14.0, 1, "MSFT", 1)
                        )
                )
        );


        if (order.getSide().equals(Side.BUY.name())) {
            // Check selling price

            // Check if order quantity exceeds allowed limit

            // Check
        }

        // Send order to exchange
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

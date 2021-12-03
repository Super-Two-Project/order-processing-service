package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;
import io.turntabl.super2.orderProcessingService.market_data.MarketData;
import io.turntabl.super2.orderProcessingService.market_data.MarketQuote;
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

    Map<String, List<MarketData>> md = new HashMap<>();

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public MarketQuote getPrice(String ticker, Side side) {
        return this.getBestPrice(side, ticker);
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
        Order order = new Order(orderRequest);

        // Check for alternative quote
        var alternativeQuote = this.getBestPrice(order.getSide(), order.getProduct());

        // Create an alternativeOrder
        Order alternativeOrder = new Order();
        alternativeOrder.setSide(order.getSide());
        alternativeOrder.setProduct(order.getProduct());
        alternativeOrder.setPrice(alternativeQuote.getPrice());
        alternativeOrder.setQuantity(order.getQuantity());

        // Response object
        ResponseEntity<String> response = null;

        if (order.getSide().equals(Side.SELL)) {
            System.out.println("selling...");
            if (alternativeOrder.getPrice() > order.getPrice()) {
                System.out.println("using alt order...");
                if (alternativeQuote.getExchange().equals("exchange1")) {
                    System.out.println("ex1");
                    response = restTemplate.postForEntity(this.exchange + "/" + this.API_KEY + "/order", alternativeOrder, String.class);
                }
                else if (alternativeQuote.getExchange().equals("exchange2")) {
                    System.out.println("ex2");
                    response = restTemplate.postForEntity(this.exchange2 + "/" + this.API_KEY + "/order", alternativeOrder, String.class);
                }
            } else {
                System.out.println("using original order...");
                if (alternativeQuote.getExchange().equals("exchange1")) {
                    System.out.println("ex1");
                    response = restTemplate.postForEntity(this.exchange + "/" + this.API_KEY + "/order", order, String.class);
                }
                else if (alternativeQuote.getExchange().equals("exchange2")) {
                    System.out.println("ex2");
                    response = restTemplate.postForEntity(this.exchange2 + "/" + this.API_KEY + "/order", order, String.class);
                }
            }
        } else {
            System.out.println("buying...");
            if (alternativeOrder.getPrice() < order.getPrice()) {
                System.out.println("using alt order...");
                if (alternativeQuote.getExchange().equals("exchange1")) {
                    System.out.println("ex1");
                    System.out.println(alternativeOrder);
                    response = restTemplate.postForEntity(this.exchange + "/" + this.API_KEY + "/order", alternativeOrder, String.class);
                }
                else if (alternativeQuote.getExchange().equals("exchange2")) {
                    System.out.println("ex2");
                    response = restTemplate.postForEntity(this.exchange2 + "/" + this.API_KEY + "/order", alternativeOrder, String.class);
                }
            } else {
                System.out.println("using original order...");
                if (alternativeQuote.getExchange().equals("exchange1")) {
                    System.out.println("ex1");
                    response = restTemplate.postForEntity(this.exchange + "/" + this.API_KEY + "/order", order, String.class);
                }
                else if (alternativeQuote.getExchange().equals("exchange2")) {
                    System.out.println("ex2");
                    response = restTemplate.postForEntity(this.exchange2 + "/" + this.API_KEY + "/order", order, String.class);
                }
            }
        }

        return response;
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

    public MarketQuote getBestPrice(Side side, String ticker) {
        List<MarketQuote> tempContainer = new ArrayList<>();

        // Get market data
        this.getMarketData();

        // Get keys of datastore
        var keys = md.keySet();

        // Loop through keys
        for (String key: keys) {
            MarketData potentialBestOffer;
            if (side.equals(Side.SELL)) {
                potentialBestOffer = md.get(key).stream()
                        .filter(marketData -> marketData.getTicker().equals(ticker))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);

                tempContainer.add(new MarketQuote(key, potentialBestOffer.getBidPrice(), potentialBestOffer.getSellLimit())
                );
            } else {
                potentialBestOffer = md.get(key).stream()
                        .filter(marketData -> marketData.getTicker().equals(ticker))
                        .findFirst()
                        .orElseThrow(RuntimeException::new);

                tempContainer.add(new MarketQuote(key, potentialBestOffer.getAskPrice(), potentialBestOffer.getBuyLimit())
                );
            }
        }

        if (side.equals(Side.SELL)) {
            return tempContainer.stream()
                    .max(Comparator.comparingDouble(MarketQuote::getPrice))
                    .orElseThrow(RuntimeException::new);
        } else {
            return tempContainer.stream()
                    .min(Comparator.comparingDouble(MarketQuote::getPrice))
                    .orElseThrow(RuntimeException::new);
        }
    }

    public void getMarketData() {
        this.md.put(
            "exchange1", List.of(
                new MarketData(1.0, 3, 16.0, 11.0, 11, "MSFT", 1),
                new MarketData(2.0, 1, 1.0, 10.0, 13, "AAPL", 1),
                new MarketData(3.0, 2, 19.0, 14.1, 1, "TSLA", 1)
            )
        );

        this.md.put(
            "exchange2", List.of(
                new MarketData(1.0, 3, 16.5, 41.0, 11, "MSFT", 1),
                new MarketData(2.0, 1, 1.0, 10.0, 13, "TSLA", 1),
                new MarketData(3.0, 2, 19.1, 14.0, 1, "AAPL", 1)
            )
        );
    }
}

package io.turntabl.super2.orderProcessingService.order;

import io.turntabl.super2.orderProcessingService.enums.Side;
import io.turntabl.super2.orderProcessingService.market_data.ExchangeMarketData;
import io.turntabl.super2.orderProcessingService.market_data.MarketData;
import io.turntabl.super2.orderProcessingService.market_data.MarketDataRepository;
import io.turntabl.super2.orderProcessingService.market_data.MarketQuote;
import io.turntabl.super2.orderProcessingService.order.exceptions.InvalidOrderException;
import io.turntabl.super2.orderProcessingService.order.exceptions.OrderNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@EnableScheduling
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

    @Autowired
    MarketDataRepository marketDataRepository;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

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

        // Check order and send to exchange
        return this.sendOrderToExchange(orderRequest.getSide(), orderRequest, alternativeQuote, alternativeOrder, order);
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
        this.getExchangeMarketData();

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

    public ResponseEntity<String> sendOrderToExchange(Side side, OrderRequest orderRequest, MarketQuote alternativeQuote, Order alternativeOrder, Order order) {
        ResponseEntity<String> response = null;
        switch (side) {
            case SELL:
                if (alternativeOrder.getPrice() > order.getPrice()) {
                    response = this.restTemplate.postForEntity(
                        this.getExchange(alternativeQuote) + "/" + this.API_KEY + "/order",
                        alternativeOrder, String.class
                    );
                } else {
                    response = this.restTemplate.postForEntity(
                        this.getExchange(orderRequest) + "/" + this.API_KEY + "/order",
                        order, String.class
                    );
                }
                break;
            case BUY:
                if (alternativeOrder.getPrice() < order.getPrice()) {
                    response = this.restTemplate.postForEntity(
                        this.getExchange(alternativeQuote) + "/" + this.API_KEY + "/order",
                        alternativeOrder, String.class
                    );
                } else {
                    response = this.restTemplate.postForEntity(
                        this.getExchange(orderRequest), 
                        order, String.class);
                }
                break;
            default:
                throw new InvalidOrderException("Invalid side");
        }

        return response;
    }

    public String getExchange(MarketQuote alternativeQuote) {
        String response = "";
        switch (alternativeQuote.getExchange()) {
            case "exchange1":
                response = this.exchange;
                break;
            case "exchange2":
                response = this.exchange2;
                break;
            default:
            throw new InvalidOrderException("Invalid exchange");
        }

        return response;
    }

    public String getExchange(OrderRequest orderRequest) {
        String response = "";
        switch (orderRequest.getExchange()) {
            case "exchange1":
                response = this.exchange;
                break;
            case "exchange2":
                response = this.exchange2;
                break;
            default:
            throw new InvalidOrderException("Invalid exchange");
        }

        return response;
    }

    public void getExchangeMarketData() {
        ExchangeMarketData ex1 = this.marketDataRepository.findById("exchange1")
                .orElseThrow(RuntimeException::new);

        ExchangeMarketData ex2 = this.marketDataRepository.findById("exchange2")
                .orElseThrow(RuntimeException::new);

        this.md.put(ex1.getId(), ex1.getMarketData());
        this.md.put(ex2.getId(), ex2.getMarketData());
    }

    @Scheduled(fixedRate = 3000)
    public void sendMessage() {
        String time = new SimpleDateFormat("mm:ss").format(new Date());
        this.simpMessagingTemplate.convertAndSend("/topic/pushmessages", Map.of("message", time));
    }


    public void getMarketData() {
        this.md.put(
            "exchange1", List.of(
                new MarketData(1.0, 3, 1.0, 1.2, 11, "MSFT", 1),
                new MarketData(2.0, 1, 1.1, 1.1, 13, "AAPL", 1),
                new MarketData(3.0, 2, 1.2, 1.0, 1, "TSLA", 1)
            )
        );

        this.md.put(
            "exchange2", List.of(
                new MarketData(1.0, 3, 1.0, 1.2, 11, "MSFT", 1),
                new MarketData(2.0, 1, 1.1, 1.1, 13, "TSLA", 1),
                new MarketData(3.0, 2, 1.2, 1.0, 1, "AAPL", 1)
            )
        );
    }
}

package io.turntabl.super2.orderProcessingService;

import io.turntabl.super2.orderProcessingService.order.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class OrderProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessingServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public WebClient webClient() {
		return WebClient
				.builder()
				.build();


//				.baseUrl(this.exchange)
//				.build()
//				.post()
//				.uri("/" + this.API_KEY + "/order")
//				.body(Mono.just(order), Order.class)
//				.retrieve()
//				.bodyToMono(String.class)
//				.subscribe(res -> System.out.println(res));
	}
}

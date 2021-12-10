package io.turntabl.super_two.order_processing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class OrderProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderProcessingServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

//	@Bean
//	public WebClient webClient() {
//		return WebClient
//				.builder()
//				.build();


//				.baseUrl(this.exchange)
//				.build()
//				.post()
//				.uri("/" + this.API_KEY + "/order")
//				.body(Mono.just(order), Order.class)
//				.retrieve()
//				.bodyToMono(String.class)
//				.subscribe(res -> System.out.println(res));
//	}
}

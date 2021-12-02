package io.turntabl.super2.orderProcessingService.market_data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class MarketDataConfiguration {

    @Bean
    ReactiveRedisOperations<String, MarketData> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<MarketData> serializer = new Jackson2JsonRedisSerializer<>(MarketData.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, MarketData> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, MarketData> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
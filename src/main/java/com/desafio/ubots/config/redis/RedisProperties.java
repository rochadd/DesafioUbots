package com.desafio.ubots.config.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedisProperties {

    private final int redisPort;
    private final String redisHost;

    public RedisProperties(@Value("${spring.data.redis.port}") final int redisPort, @Value("${spring.data.redis.host}") final String redisHost) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
}

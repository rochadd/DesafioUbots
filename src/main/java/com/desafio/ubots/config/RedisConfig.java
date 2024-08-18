package com.desafio.ubots.config;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import com.desafio.ubots.config.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(final RedisProperties redisProperties) {
        return new LettuceConnectionFactory(redisProperties.getRedisHost(), redisProperties.getRedisPort());
    }

    @Bean
    public RedisTemplate<String, Request> redisTemplate(final LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Request> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Use String serialization for the key
        template.setKeySerializer(new StringRedisSerializer());

        // Use JSON serialization for the value
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}

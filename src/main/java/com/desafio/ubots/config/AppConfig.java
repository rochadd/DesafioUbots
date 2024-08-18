package com.desafio.ubots.config;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class AppConfig {

    private static final int CONCURRENT_REQUESTS = 3; // Max concurrent requests per topic

    @Bean
    public BlockingQueue<Request> cartoesQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Request> emprestimosQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public BlockingQueue<Request> outrosQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public Semaphore cartoesSemaphore() {
        return new Semaphore(CONCURRENT_REQUESTS);
    }

    @Bean
    public Semaphore emprestimosSemaphore() {
        return new Semaphore(CONCURRENT_REQUESTS);
    }

    @Bean
    public Semaphore outrosSemaphore() {
        return new Semaphore(CONCURRENT_REQUESTS);
    }

    @Bean
    public ExecutorService cartoesExecutorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public ExecutorService emprestimosExecutorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public ExecutorService outrosExecutorService() {
        return Executors.newFixedThreadPool(10);
    }
}

package com.desafio.ubots.component;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static com.desafio.ubots.com.desafio.ubots.model.RequestType.*;

@Component
@EnableScheduling
public class RequestProcessorScheduler {

    @Autowired
    private RedisTemplate<String, Request> redisTemplate;

    @Autowired
    private BlockingQueue<Request> cartoesQueue;

    @Autowired
    private BlockingQueue<Request> emprestimosQueue;

    @Autowired
    private BlockingQueue<Request> outrosQueue;

    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    public void processRequests() {
        processQueue(CARTAO.getSubject(), cartoesQueue);
        processQueue(EMPRESTIMO.getSubject(), emprestimosQueue);
        processQueue(OUTROS.getSubject(), outrosQueue);
    }

    private void processQueue(String redisKey, BlockingQueue<Request> queue) {
        Map<Object, Object> requests = redisTemplate.opsForHash().entries(redisKey);

        for (Map.Entry<Object, Object> entry : requests.entrySet()) {
            Request request = (Request) entry.getValue();
            try {
                queue.put(request);  // Put the request into the respective queue
                redisTemplate.opsForHash().delete(redisKey, entry.getKey());  // Remove the request from Redis
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}

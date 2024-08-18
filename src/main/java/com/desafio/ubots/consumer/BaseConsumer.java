package com.desafio.ubots.consumer;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class BaseConsumer {

    private final BlockingQueue<Request> queue;
    private final Semaphore semaphore;
    private final ExecutorService executorService;

    protected BaseConsumer(BlockingQueue<Request> queue, Semaphore semaphore, ExecutorService executorService) {
        this.queue = queue;
        this.semaphore = semaphore;
        this.executorService = executorService;
    }

    protected void consumer() {
        while (true) {
            try {
                if (!queue.isEmpty()) {
                    Request request = queue.take(); // Take request from the queue
                    if (semaphore.tryAcquire()) { // Attempt to acquire a permit from the semaphore
                        executorService.submit(() -> {
                            try {
                                // Simulate request processing
                                log.info("Processing {} Request: {}", getClass().getSimpleName() , request.getContent());
                                TimeUnit.SECONDS.sleep(2); // Simulate some processing time
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            } finally {
                                semaphore.release(); // Release the permit after processing
                            }
                        });
                    } else {
                        // If the semaphore is not available, return the request to the queue
                        log.info("Consumer busy, returning request to queue: {}", request.getContent());
                        queue.put(request); // Re-enqueue the request
                        try {
                            TimeUnit.SECONDS.sleep(1); // waiting next round
                        } catch (InterruptedException e) {
                            log.error("Something went wrong here ... {} ", e.getMessage());
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

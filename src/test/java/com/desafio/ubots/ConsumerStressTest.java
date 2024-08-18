package com.desafio.ubots;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import com.desafio.ubots.com.desafio.ubots.model.RequestType;
import com.desafio.ubots.consumer.CartoesConsumer;
import com.desafio.ubots.consumer.EmprestimosConsumer;
import com.desafio.ubots.consumer.OutrosConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.*;

class ConsumerStressTest {

    private final BlockingQueue<Request> cartoesQueue = new LinkedBlockingQueue<>(40);
    private final BlockingQueue<Request> emprestimosQueue = new LinkedBlockingQueue<>(40);
    private final BlockingQueue<Request> outrosQueue = new LinkedBlockingQueue<>(40);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Semaphore cartoesSemaphore = new Semaphore(3); // Limit to 3 concurrent requests
        Semaphore emprestimosSemaphore = new Semaphore(3); // Limit to 3 concurrent requests
        Semaphore outrosSemaphore = new Semaphore(3); // Limit to 3 concurrent requests

        ExecutorService cartoesExecutorService = Executors.newFixedThreadPool(10);
        ExecutorService emprestimosExecutorService = Executors.newFixedThreadPool(10);
        ExecutorService outrosExecutorService = Executors.newFixedThreadPool(10);

        CartoesConsumer cartoesConsumer = new CartoesConsumer(cartoesQueue, cartoesSemaphore, cartoesExecutorService);
        EmprestimosConsumer emprestimosConsumer = new EmprestimosConsumer(emprestimosQueue, emprestimosSemaphore, emprestimosExecutorService);
        OutrosConsumer outrosConsumer = new OutrosConsumer(outrosQueue, outrosSemaphore, outrosExecutorService);

        // Start multiple threads for CartoesConsumer
        for (int i = 0; i < 5; i++) {
            new Thread(cartoesConsumer).start();
        }

        // Start multiple threads for EmprestimosConsumer
        for (int i = 0; i < 5; i++) {
            new Thread(emprestimosConsumer).start();
        }

        // Start multiple threads for OutrosConsumer
        for (int i = 0; i < 5; i++) {
            new Thread(outrosConsumer).start();
        }
    }

    @Test
    void stressTestConsumers() throws InterruptedException {
        int numRequests = 100; // Total number of requests to simulate

        // Simulate multiple requests
        for (int i = 0; i < numRequests; i++) {
            String requestContent = "Request " + (i + 1);
            if (i % 3 == 0) {
                cartoesQueue.put(new Request(RequestType.CARTAO, requestContent));
            } else if (i % 3 == 1) {
                emprestimosQueue.put(new Request(RequestType.EMPRESTIMO, requestContent));
            } else {
                outrosQueue.put(new Request(RequestType.OUTROS, requestContent));
            }
        }

        System.out.println("CartaoQueue size: " + cartoesQueue.size());
        System.out.println("EmprestimoQueue size: " + emprestimosQueue.size());
        System.out.println("OutroQueue size: " + outrosQueue.size());

        // Give enough time for all requests to be processed
        Thread.sleep(30000);


        Assertions.assertTrue(cartoesQueue.isEmpty());
        Assertions.assertTrue(emprestimosQueue.isEmpty());
        Assertions.assertTrue(outrosQueue.isEmpty());
    }
}
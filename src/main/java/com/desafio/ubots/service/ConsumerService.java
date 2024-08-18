package com.desafio.ubots.service;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import com.desafio.ubots.consumer.CartoesConsumer;
import com.desafio.ubots.consumer.EmprestimosConsumer;
import com.desafio.ubots.consumer.OutrosConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class ConsumerService {

    private final BlockingQueue<Request> cartoesQueue;
    private final Semaphore cartoesSemaphore;
    private final ExecutorService cartoesExecutorService;

    private final BlockingQueue<Request> emprestimosQueue;
    private final Semaphore emprestimosSemaphore;
    private final ExecutorService emprestimosExecutorService;

    private final BlockingQueue<Request> outrosQueue;
    private final Semaphore outrosSemaphore;
    private final ExecutorService outrosExecutorService;

    @Autowired
    public ConsumerService(BlockingQueue<Request> cartoesQueue, Semaphore cartoesSemaphore, ExecutorService cartoesExecutorService,
                           BlockingQueue<Request> emprestimosQueue, Semaphore emprestimosSemaphore, ExecutorService emprestimosExecutorService,
                           BlockingQueue<Request> outrosQueue, Semaphore outrosSemaphore, ExecutorService outrosExecutorService) {
        this.cartoesQueue = cartoesQueue;
        this.cartoesSemaphore = cartoesSemaphore;
        this.cartoesExecutorService = cartoesExecutorService;
        this.emprestimosQueue = emprestimosQueue;
        this.emprestimosSemaphore = emprestimosSemaphore;
        this.emprestimosExecutorService = emprestimosExecutorService;
        this.outrosQueue = outrosQueue;
        this.outrosSemaphore = outrosSemaphore;
        this.outrosExecutorService = outrosExecutorService;
    }

    @PostConstruct
    public void startConsumers() {
        // Start multiple threads for CartoesConsumer
        startCartaoConsumer();
        startEmprestimoConsumer();
        startOutroConsumer();
    }

    private ExecutorService createExecutor(String threadName) {
        return Executors.newFixedThreadPool(5, new ThreadFactory() {
            private int counter = 1;

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName(threadName + "-" + counter++);
                return thread;
            }
        });
    }

    private void startCartaoConsumer() {
        ExecutorService cartaoExecutorService = createExecutor("CartaoConsumer");

        for (int i = 0; i < 5; i++) {
            cartaoExecutorService.submit(new CartoesConsumer(cartoesQueue, cartoesSemaphore, cartoesExecutorService));
        }
    }

    private void startEmprestimoConsumer() {
        ExecutorService emprestimoExecutorService = createExecutor("EmprestimoConsumer");

        for (int i = 0; i < 5; i++) {
            emprestimoExecutorService.submit(new EmprestimosConsumer(emprestimosQueue, emprestimosSemaphore, emprestimosExecutorService));
        }
    }

    private void startOutroConsumer() {
        ExecutorService outroExecutorService = createExecutor("OutroConsumer");

        for (int i = 0; i < 5; i++) {
            outroExecutorService.submit(new OutrosConsumer(outrosQueue, outrosSemaphore, outrosExecutorService));
        }
    }
}
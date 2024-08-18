package com.desafio.ubots.consumer;

import com.desafio.ubots.com.desafio.ubots.model.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class CartoesConsumer extends BaseConsumer implements Runnable {

    public CartoesConsumer(BlockingQueue<Request> cartoesQueue, Semaphore semaphore, ExecutorService executorService) {
        super(cartoesQueue, semaphore, executorService);
    }

    @Override
    public void run() {
        consumer();
    }
}

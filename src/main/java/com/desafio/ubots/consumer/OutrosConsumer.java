package com.desafio.ubots.consumer;

import com.desafio.ubots.com.desafio.ubots.model.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class OutrosConsumer extends BaseConsumer implements Runnable {

    public OutrosConsumer(BlockingQueue<Request> outrosQueue, Semaphore semaphore, ExecutorService executorService) {
        super(outrosQueue, semaphore, executorService);
    }

    @Override
    public void run() {
        consumer();
    }
}

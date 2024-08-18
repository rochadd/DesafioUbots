package com.desafio.ubots.consumer;

import com.desafio.ubots.com.desafio.ubots.model.Request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

public class EmprestimosConsumer extends BaseConsumer implements Runnable {

    public EmprestimosConsumer(BlockingQueue<Request> emprestimosQueue, Semaphore semaphore, ExecutorService executorService) {
        super(emprestimosQueue, semaphore, executorService);
    }

    @Override
    public void run() {
       consumer();
    }
}

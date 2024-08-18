package com.desafio.ubots;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import com.desafio.ubots.com.desafio.ubots.model.RequestType;
import com.desafio.ubots.consumer.CartoesConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import static org.mockito.Mockito.*;

class CartoesConsumerTest {

    @Mock
    private BlockingQueue<Request> cartoesQueue;

    @Mock
    private Semaphore semaphore;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private CartoesConsumer cartoesConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldProcessRequestWhenSemaphoreIsAvailable() throws InterruptedException {
        Request request = new Request(RequestType.CARTAO, "Request 1");
        when(cartoesQueue.take()).thenReturn(request);
        when(semaphore.tryAcquire()).thenReturn(true);

        doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(executorService).submit(ArgumentMatchers.any(Runnable.class));

        new Thread(cartoesConsumer).start();
        Thread.sleep(8000); // Allow time for processing

        verify(executorService, atLeast(3)).submit(ArgumentMatchers.any(Runnable.class));
        verify(semaphore, atLeast(3)).release();
    }

    @Test
    void shouldReturnRequestToQueueWhenConsumerIsBusy() throws InterruptedException {
        Request request = new Request(RequestType.CARTAO, "Request 2");
        when(cartoesQueue.take()).thenReturn(request);
        when(semaphore.tryAcquire()).thenReturn(false); // Simulate consumer being busy

        new Thread(cartoesConsumer).start();
        Thread.sleep(500); // Allow time for processing

        verify(cartoesQueue, atLeastOnce()).put(request); // Ensure the request was re-enqueued
    }
}

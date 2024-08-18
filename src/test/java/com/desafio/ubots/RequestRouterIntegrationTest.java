package com.desafio.ubots;

import com.desafio.ubots.com.desafio.ubots.model.Request;
import com.desafio.ubots.com.desafio.ubots.model.RequestType;
import com.desafio.ubots.component.RequestProcessorScheduler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestRedisConfiguration.class)
public class RequestRouterIntegrationTest {

    @Autowired
    private RedisTemplate<String, Request> redisTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BlockingQueue<Request> cartoesQueue;

    @Autowired
    private BlockingQueue<Request> emprestimosQueue;

    @Autowired
    private BlockingQueue<Request> outrosQueue;

    @Autowired
    private RequestProcessorScheduler requestProcessorService;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll(); // Clear Redis before each test
    }

    @Test
    void handleRequests() throws InterruptedException {
        // 1. Simulate incoming requests
        Request cartaoRequest = new Request(RequestType.CARTAO, "Cartao Request Content 1");
        Request emprestimoRequest = new Request(RequestType.EMPRESTIMO, "Emprestimo Request Content 1");
        Request outrosRequest = new Request(RequestType.OUTROS, "Outros Request Content 1");

        restTemplate.postForEntity("/api/requests", cartaoRequest, String.class);
        restTemplate.postForEntity("/api/requests", emprestimoRequest, String.class);
        restTemplate.postForEntity("/api/requests", outrosRequest, String.class);

        Map<Object, Object> cartaoRedisKeyBefore = redisTemplate.opsForHash().entries(RequestType.CARTAO.getSubject());
        Map<Object, Object> emprestimoRedisKeyBefore = redisTemplate.opsForHash().entries(RequestType.EMPRESTIMO.getSubject());
        Map<Object, Object> outroRedisKeyBefore = redisTemplate.opsForHash().entries(RequestType.OUTROS.getSubject());

        Assertions.assertNotNull(cartaoRequest);
        Assertions.assertEquals(1, cartaoRedisKeyBefore.size());

        Assertions.assertNotNull(emprestimoRedisKeyBefore);
        Assertions.assertEquals(1, emprestimoRedisKeyBefore.size());

        Assertions.assertNotNull(outroRedisKeyBefore);
        Assertions.assertEquals(1, outroRedisKeyBefore.size());

        LocalDateTime date = LocalDateTime.now().plus(1, TimeUnit.SECONDS.toChronoUnit());
        Instant instant = date.atZone(ZoneOffset.UTC).toInstant();

        // 2. Trigger the cron job manually (simulating time passing)
        taskScheduler.schedule(() -> requestProcessorService.processRequests(), instant);
        TimeUnit.SECONDS.sleep(5); // Wait for cron job to execute

        // 3. Verify that requests are placed in the correct queues
        Map<Object, Object> cartaoRedisKeyAfter = redisTemplate.opsForHash().entries(RequestType.CARTAO.getSubject());
        Map<Object, Object> emprestimoRedisKeyAfter = redisTemplate.opsForHash().entries(RequestType.EMPRESTIMO.getSubject());
        Map<Object, Object> outroRedisKeyAfter = redisTemplate.opsForHash().entries(RequestType.OUTROS.getSubject());

        Assertions.assertNotNull(cartaoRedisKeyAfter);
        Assertions.assertEquals(0, cartaoRedisKeyAfter.size());

        Assertions.assertNotNull(emprestimoRedisKeyAfter);
        Assertions.assertEquals(0, emprestimoRedisKeyAfter.size());

        Assertions.assertNotNull(outroRedisKeyAfter);
        Assertions.assertEquals(0, outroRedisKeyAfter.size());
    }
}

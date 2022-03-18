package ru.pkalita.cache.storage;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadSafeCachedStoreTest {

    public static final int CACHE_SIZE = 500;
    public static final int NUM_THREADS = 10;
    public static final int NUM_INSERTS = 1000;
    public static final String VALUE_NAME = "value";

    @Test
    void dataMustNotBeLostWhenMultiThread() throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        IStorage cache = new CachedStore(new ThreadSafeCachedStore(CACHE_SIZE));
        CountDownLatch countDownLatch = new CountDownLatch(CACHE_SIZE);
        try {
            IntStream.range(0, CACHE_SIZE).<Runnable>mapToObj(key -> () -> {
                cache.put(String.valueOf(key), VALUE_NAME + key);
                countDownLatch.countDown();
            }).forEach(executorService::submit);
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }
        assertEquals(CACHE_SIZE, cache.size());
        IntStream.range(0, CACHE_SIZE).forEach(i -> assertEquals(VALUE_NAME + i, cache.get(String.valueOf(i))));
    }

    @Test
    void cacheMustNotBeOverflowed() throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        IStorage cache = new CachedStore(new ThreadSafeCachedStore(CACHE_SIZE));
        CountDownLatch countDownLatch = new CountDownLatch(NUM_INSERTS);
        try {
            IntStream.range(0, NUM_INSERTS).<Runnable>mapToObj(key -> () -> {
                cache.put(String.valueOf(key), VALUE_NAME + key);
                countDownLatch.countDown();
            }).forEach(executorService::submit);
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }
        assertEquals(CACHE_SIZE, cache.size());
    }
}
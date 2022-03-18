package ru.pkalita.cache.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadSafeCachedStore implements IStorage {

    private static final Logger LOG = Logger.getLogger(ThreadSafeCachedStore.class.getName());

    private final Map<String, String> cache;
    private final ReadWriteLock lock;
    private final int maxSize;

    public ThreadSafeCachedStore(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new ConcurrentHashMap<>(maxSize);
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void put(String key, String value) {
        lock.writeLock().lock();
        if (this.maxSize < 1) {
            LOG.severe("Cache size must be greater than 0");
            return;
        }
        try {
            if (!cache.containsKey(key) && cache.size() >= this.maxSize) {
                evictElement();
            }
            this.cache.put(key, value);
            LOG.log(Level.INFO, "Element {0} put in the cache", value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String get(String key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /*
    Here is a simple implementation of to evict elements from the cache, which removes the first element found.
    It is possible to remove the oldest element by complicating model, by storing values, for example, in linked list
    To do this we also need to implement moving cached elements to the top when we get or update elements
    */
    private void evictElement() {
        this.lock.writeLock().lock();
        try {
            String keyToRemove = cache.entrySet().iterator().next().getKey();
            String valueToRemove = cache.entrySet().iterator().next().getValue();
            cache.remove(keyToRemove);
            LOG.log(Level.INFO, "Element {0} evicted from the cache", valueToRemove);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}

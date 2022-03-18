package ru.pkalita.cache.storage;

public interface IStorage {

    /**
     * Inserting data to cache
     *
     * @param key key
     * @param value value
     */
    void put(String key, String value);

    /**
     * Getting data from cache
     *
     * @param key key
     * @return value
     */
    String get(String key);

    /**
     * Getting actual cache size
     * @return cache size
     */
    int size();
}

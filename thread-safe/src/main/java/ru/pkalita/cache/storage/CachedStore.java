package ru.pkalita.cache.storage;

public class CachedStore implements IStorage {

    private final IStorage storage;

    public CachedStore(IStorage storage) {
        this.storage = storage;
    }

    @Override
    public void put(String key, String value) {
        storage.put(key, value);
    }

    @Override
    public String get(String key) {
        return storage.get(key);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
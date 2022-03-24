# Test task

Implement thread-safe cache with limited number of cached elements.

Assume we have an interface that represents some external key-value storage:

    interface IStorage {

        void put(String key, String value);

        String get(String key);

    }

And we need to implement thread-safe cache based on the storage. The cache should:

- Implement the same IStorage interface by delegating calls to the underlying external key-value storage (storage)

- Store not more than specified (maxSize) elements in memory

- It should be thread-safe. It could be used from multiple different threads simultaneously. And it should guarantee that data stored in memory is always aligned with the external storage, there are not more than maxSize elements in the cache.

- Try to think about better synchronization across different threads to optimize performance of the cache.


    class CachedStore implements IStorage {

        private int maxSize;
        private IStorage storage;

        public CachedStore(IStorage storage, int maxSize) {
            this.storage = storage;
            this.size = maxSize;
        }

        public void put(String key, String value) {
            //?
        }

        public String get(String key) {
            //?
        }
    }

The solution should be a small console application with the cache 
implementation and an example how the cache could be used.
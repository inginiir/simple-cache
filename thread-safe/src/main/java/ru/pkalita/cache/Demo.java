package ru.pkalita.cache;

import ru.pkalita.cache.storage.CachedStore;
import ru.pkalita.cache.storage.IStorage;
import ru.pkalita.cache.storage.ThreadSafeCachedStore;

import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        System.out.println("Enter cache size");
        Scanner scanner = new Scanner(System.in);
        int cacheSize = scanner.nextInt();
        IStorage storage = new CachedStore(new ThreadSafeCachedStore(cacheSize));

        for (int j = 0; j < cacheSize + 1; j++) {
            System.out.printf("Enter %d value to cache%n", j + 1);
            String value = scanner.next();
            storage.put(String.valueOf(j + 1), value);
        }
        System.out.printf("There are %d elements in the cache%n", storage.size());
        for (int i = 0; i < cacheSize + 1; i++) {
            String key = String.valueOf(i + 1);
            String value = storage.get(key);
            if (value != null) {
                System.out.printf("%s=%s%n", key, value);
            }
        }
    }
}

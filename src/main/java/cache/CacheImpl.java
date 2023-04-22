package cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CacheImpl<T> implements Cache<T> {
    public void raceCondition() {
        List<String> f = new ArrayList<>();

        CompletableFuture c1 = CompletableFuture.supplyAsync(() -> {
            f.add("ss");
            return "";
        });
        CompletableFuture c2 = CompletableFuture.supplyAsync(() -> {
            f.add("ss");
            return "";
        });
        CompletableFuture c3 = CompletableFuture.supplyAsync(() -> {
            f.add("ss");
            return "";
        });
        c1.join();
        c2.join();
        c3.join();

    }

    private int increase(final int target, final int count) {
//        final int res;

        return IntStream.range(0, count).sum();

//        for (int i = 0; i < count; i++) {
//            res = res + 1;
//        }
//        return res;
    }

    private int increaseRecursive(final int target, final int count) {
        if (count == 0) {
            return target;
        }

        return increaseRecursive(target + 1, count - 1);
    }
    public static final long PERMANENT = -1L;

    private static final CacheImpl<?> INSTANCE = new CacheImpl<>();

    private final ConcurrentHashMap<String, T> caches;
    private final ConcurrentHashMap<String, Long> cacheTTL;
//    private final ScheduledExecutorService executorServiceForTTL;

    public CacheImpl() {
        this.caches = new ConcurrentHashMap<>();
        this.cacheTTL = new ConcurrentHashMap<>();
//        this.executorServiceForTTL = Executors.newSingleThreadScheduledExecutor();
//        removeExpiredTimeKey();
    }

    public static <T> CacheImpl<T> getInstance() {
        return (CacheImpl<T>) INSTANCE;
    }

//    private void removeExpiredTimeKey() {
//        executorServiceForTTL.scheduleAtFixedRate(() -> cacheTTL.entrySet().removeIf(entry -> {
//            if (isExpiration(entry.getValue())) {
//                caches.remove(entry.getKey());
//                return true;
//            }
//            return false;
//        }), 1, 1, TimeUnit.SECONDS);
//    }

    public T myCacheable(String key, Supplier<T> dataValue) {
        return myCacheable(key, dataValue, PERMANENT);
    }

    public T myCacheable(String key, Supplier<T> dataValue, long ttl) {
        return Optional.ofNullable(get(key))
                .orElseGet(() -> {
                    T value = dataValue.get();
                    put(key, value, ttl);
                    return value;
                });
    }

    public List<T> myBulkCacheable(List<String> keys, Supplier<Map<String, T>> dataValues, long ttl) {
        List<T> existingValues = multiGet(keys);
        
        List<String> missingKeys = keys.stream()
                .filter(key -> existingValues.get(keys.indexOf(key)) == null)
                .collect(Collectors.toList());

        if (!missingKeys.isEmpty()) {
            missingKeys.stream().forEach(key -> {
                T value = dataValues.get().get(key);
                if (value != null) {
                    put(key, value, ttl);
                    existingValues.set(keys.indexOf(key), value);
                }
            });
        }

        return existingValues;
    }

    public List<T> multiGet(List<String> keys) {
        return keys.stream()
                .map(key -> get(key))
                .collect(Collectors.toList());
    }
    
//    public void multiPut(List<CacheKeyValue<T>> keyValues) {
//        keyValues.forEach(e -> caches.put(e.key, e.value));
//    }
    

    @Override
    public void put(String key, T value) {
        put(key, value, PERMANENT);
    }

    @Override
    public void put(String key, T value, long ttl) {
        caches.put(key, value);
        if (isPermanent(ttl)) {
            cacheTTL.put(key, PERMANENT);
        } else {
            cacheTTL.put(key, System.currentTimeMillis() + ttl * 1000);
        }
    }

    @Override
    public T get(String key) {
        T value = caches.get(key);
        if (value == null) {
            return null;
        }

        long expiration = cacheTTL.get(key);
        if (isExpiration(expiration)) {
            return null;
        }

        return value;
    }

    @Override
    public Long getExpirationTimeToMillis(String key) {
        T cache = caches.get(key);
        if (cache == null) {
            return null;
        }

        long expiration = cacheTTL.get(key);
        if (isExpiration(expiration)) {
            return null;
        }

        return expiration;
    }

    private boolean isExpiration(long expiration) {
        if (isPermanent(expiration)) {
            return false;
        }

        return System.currentTimeMillis() > expiration;
    }

    private boolean isPermanent(long ttl) {
        return ttl == PERMANENT;
    }

    @Override
    public String toString() {
        return "Cache{" +
                "cache=" + caches +
                ", cacheTTL=" + cacheTTL +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheImpl<?> cacheImpl1 = (CacheImpl<?>) o;
        return Objects.equals(caches, cacheImpl1.caches) && Objects.equals(cacheTTL, cacheImpl1.cacheTTL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caches, cacheTTL);
    }
}

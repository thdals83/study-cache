package cache;

import java.util.Optional;

public interface Cache<T> {
    
    void put(String key, T value);

    void put(String key, T value, long ttl);

    T get(String key);

    Long getExpirationTimeToMillis(String key);
}

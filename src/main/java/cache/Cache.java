package cache;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Cache<T> {
    
    void put(String key, T value);

    void put(String key, T value, long ttl);

    void multiPut(Map<String, T> keyValues, long ttl);

    T get(String key);
    
    List<T> multiGet(List<String> keys);

    Long getExpirationTimeToMillis(String key);
}

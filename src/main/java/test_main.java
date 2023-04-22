
import cache.CacheImpl;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class test_main {
    public static <T> void main(String[] args) throws InterruptedException {
        //        List<CompletableFuture> cs = new ArrayList<>();
        //        Map<String ,String> f = new HashMap<>();
//        ExecutorService es = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 10000; i++) {
//            cs.add(CompletableFuture.supplyAsync(() -> {
//                System.out.println("xxx");
//                String t = f.get("test");
//                f.put("test", "good");
//                System.out.println(t);
//                return "";
//            }, es));
//        }
        //        cacheImpl.raceCondition();
//        
//        cs.forEach(CompletableFuture::join);
        
        final CacheImpl<String> cacheImpl = CacheImpl.getInstance();
        cacheImpl.put("1", test());
        cacheImpl.put("2", test());
        cacheImpl.put("3", test());
        
        List<String> keys = List.of("1", "2", "3", "4", "5");
        System.out.println(cacheImpl.get("1"));
        System.out.println(cacheImpl.get("2"));
        System.out.println(cacheImpl.get("3"));
        System.out.println("---------------------");
        
        Supplier<Map<String, String>> test = () -> {
            Map<String, String> aatt = new HashMap<>();
            aatt.put("1", test());
            aatt.put("2", test());
            aatt.put("3", test());
            aatt.put("4", test());
            aatt.put("5", test());
            return aatt;
        };
        
        cacheImpl.myBulkCacheable(keys, test, -1L);
        System.out.println(cacheImpl.get("1"));
        System.out.println(cacheImpl.get("2"));
        System.out.println(cacheImpl.get("3"));
        System.out.println(cacheImpl.get("4"));
        System.out.println(cacheImpl.get("5"));
//        String value2 = cacheImpl.myCacheable("test", test_main::test);
//        System.out.println(value2);
//
//        String value3 = cacheImpl.myCacheable("test", test_main::test, 1L);
//        System.out.println(value3);
        
//        Map<String, Supplier<T>> dataValues = new HashMap<>();
//        dataValues.put("1", () -> (T) test());
//        dataValues.put("2", () -> (T) test2());
//
//        cacheImpl.myBulkCacheable(dataValues, 10).entrySet().stream().forEach(
//                System.out::println
//        );
    }

    public static String test() {
        double res = Math.random() * 10;
        return String.valueOf(res);
    }
}

class Car {
    String carBrand;
    int speed;

    public Car(String carBrand, int speed) {
        this.carBrand = carBrand;
        this.speed = speed;
    }
}
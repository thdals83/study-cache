package com.example.studycache.controller;

import cache.CacheImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        final CacheImpl<Integer> cache = CacheImpl.getInstance();
        cache.put("1", 1);
        cache.put("2", 2);
        
        final Integer res = (Integer) cache.get("1");
        
        return String.valueOf(res);
    }


    @GetMapping("/hello2")
    public String hello2() {
        final CacheImpl<Integer> cache = CacheImpl.getInstance();
        final Integer res = (Integer) cache.get("1");

        return String.valueOf(res);
    }
}
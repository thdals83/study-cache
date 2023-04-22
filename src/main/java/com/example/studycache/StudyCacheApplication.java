package com.example.studycache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StudyCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyCacheApplication.class, args);
    }

}

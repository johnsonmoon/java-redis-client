package com.github.johnsonmoon.java.redis.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Create by xuyh at 2020/3/16 15:04.
 */
@ServletComponentScan
@SpringBootApplication
public class RedisClientApplication {
    public static void main(String[] args) {
        new SpringApplication(RedisClientApplication.class).run(args);
    }
}

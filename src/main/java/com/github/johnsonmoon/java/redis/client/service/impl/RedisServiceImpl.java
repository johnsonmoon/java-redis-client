package com.github.johnsonmoon.java.redis.client.service.impl;

import com.github.johnsonmoon.java.redis.client.entity.OperationParam;
import com.github.johnsonmoon.java.redis.client.service.RedisService;
import com.github.johnsonmoon.java.redis.client.util.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Create by xuyh at 2021/1/8 14:47.
 */
@Service
public class RedisServiceImpl implements RedisService {
    private RedisManager redisManager;

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.db}")
    private String db;

    @PostConstruct
    public void init() {
        if (redisManager == null) {
            redisManager = new RedisManager();
            redisManager.init(host, Integer.parseInt(port), password, Integer.parseInt(db));
        }
    }

    @PreDestroy
    public void shutdown() {
        if (redisManager != null) {
            redisManager.destroy();
        }
    }

    @Override
    public List<String> supportedFuncs() {
        return RedisManager.getMethodFlagList();
    }

    @Override
    public Object operate(OperationParam param) {
        if (redisManager == null || redisManager.isConnected()) {
            init();
        }
        return redisManager.operate(param.getMethodName(), param.getArgTypes(), param.getArgs());
    }
}

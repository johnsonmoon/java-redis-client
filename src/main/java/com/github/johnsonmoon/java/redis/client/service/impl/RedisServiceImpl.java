package com.github.johnsonmoon.java.redis.client.service.impl;

import com.github.johnsonmoon.java.redis.client.common.Configuration;
import com.github.johnsonmoon.java.redis.client.common.ConfigurationModifiedEventSource;
import com.github.johnsonmoon.java.redis.client.common.Configurations;
import com.github.johnsonmoon.java.redis.client.entity.OperationParam;
import com.github.johnsonmoon.java.redis.client.service.RedisService;
import com.github.johnsonmoon.java.redis.client.common.RedisManager;
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
        Configurations.getInstance().putConf(Configuration.REDIS_HOST, host);
        Configurations.getInstance().putConf(Configuration.REDIS_PORT, port);
        Configurations.getInstance().putConf(Configuration.REDIS_PASSWORD, password);
        Configurations.getInstance().putConf(Configuration.REDIS_DB, db);
        if (redisManager == null) {
            redisManager = new RedisManager();
            redisManager.init(host, Integer.parseInt(port), password, Integer.parseInt(db));
        }
        ConfigurationModifiedEventSource.addListener(() -> {
            if (redisManager != null) {
                redisManager.destroy();
            }
            redisManager = new RedisManager();
            redisManager.init(
                    String.valueOf(Configurations.getInstance().getConf(Configuration.REDIS_HOST)),
                    Integer.parseInt(String.valueOf(Configurations.getInstance().getConf(Configuration.REDIS_PORT))),
                    String.valueOf(Configurations.getInstance().getConf(Configuration.REDIS_PASSWORD)),
                    Integer.parseInt(String.valueOf(Configurations.getInstance().getConf(Configuration.REDIS_DB)))
            );
        });
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

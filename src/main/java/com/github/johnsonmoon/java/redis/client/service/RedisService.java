package com.github.johnsonmoon.java.redis.client.service;

import com.github.johnsonmoon.java.redis.client.entity.OperationParam;

import java.util.List;

/**
 * Create by xuyh at 2021/1/8 14:47.
 */
public interface RedisService {
    List<String> supportedFuncs();

    Object operate(OperationParam param);
}

package com.github.johnsonmoon.java.redis.client.common;

/**
 * Create by xuyh at 2021/3/22 10:53.
 */
public enum Configuration {
    REDIS_HOST("redis.host"),
    REDIS_PORT("redis.port"),
    REDIS_PASSWORD("redis.password"),
    REDIS_DB("redis.db");
    private String code;
    private Object value;

    Configuration(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

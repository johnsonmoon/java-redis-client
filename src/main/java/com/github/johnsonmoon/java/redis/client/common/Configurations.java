package com.github.johnsonmoon.java.redis.client.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Create by xuyh at 2021/3/22 10:51.
 */
public class Configurations {
    private static Configurations instance;

    private Map<String, Object> map = new LinkedHashMap<>();

    public void putConf(String key, Object value) {
        map.put(key, value);
    }

    public void putConf(Configuration key, Object value) {
        putConf(key.getCode(), value);
    }

    public void removeConf(String key) {
        map.remove(key);
    }

    public Object getConf(String key) {
        return map.get(key);
    }

    public Object getConf(Configuration configuration) {
        return getConf(configuration.getCode());
    }

    public List<String> getConfKeys() {
        List<String> keys = new ArrayList<>();
        map.forEach((k, v) -> keys.add(k));
        return keys;
    }

    public void forEach(BiConsumer<? super String, ? super Object> action) {
        map.forEach(action);
    }

    private Configurations() {
    }

    public static Configurations getInstance() {
        if (instance == null) {
            instance = new Configurations();
        }
        return instance;
    }
}

package com.github.johnsonmoon.java.redis.client.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by xuyh at 2020/3/16 15:23.
 */
public class MapUtils {
    private Map<String, Object> map = new HashMap<>();

    public static MapUtils builder() {
        return new MapUtils();
    }

    public MapUtils put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return map;
    }
}

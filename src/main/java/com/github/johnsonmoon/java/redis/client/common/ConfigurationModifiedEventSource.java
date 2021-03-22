package com.github.johnsonmoon.java.redis.client.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by xuyh at 2021/3/22 11:45.
 */
public class ConfigurationModifiedEventSource {
    private static final List<ConfigurationModifiedEventListener> eventListener = new ArrayList<>();

    public static void addListener(ConfigurationModifiedEventListener listener) {
        eventListener.add(listener);
    }

    public static void removeListener(ConfigurationModifiedEventListener listener) {
        eventListener.remove(listener);
    }

    public static void doOnModified() {
        if (!eventListener.isEmpty()) {
            for (ConfigurationModifiedEventListener listener : eventListener) {
                listener.onModified();
            }
        }
    }
}

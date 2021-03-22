package com.github.johnsonmoon.java.redis.client.common;

import java.util.EventListener;

/**
 * Create by xuyh at 2021/3/22 11:40.
 */
public interface ConfigurationModifiedEventListener extends EventListener {
    void onModified();
}

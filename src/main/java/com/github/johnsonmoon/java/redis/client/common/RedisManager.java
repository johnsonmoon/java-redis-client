package com.github.johnsonmoon.java.redis.client.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.lang.reflect.Method;
import java.text.Collator;
import java.util.*;

public class RedisManager {
    private static Logger logger = LoggerFactory.getLogger(RedisManager.class);

    static {
        initOperateFunc();
    }

    private static Map<String, Method> methodMap;
    private static List<String> methodFlagList;

    private static void initOperateFunc() {
        methodMap = new HashMap<>();
        methodFlagList = new ArrayList<>();
        Method[] methods = Jedis.class.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            Class[] argTypes = method.getParameterTypes();
            StringBuilder key = new StringBuilder();
            StringBuilder flag = new StringBuilder();
            key.append(methodName);
            flag.append(methodName);
            if (argTypes != null && argTypes.length > 0) {
                flag.append("(");
                for (Class argType : argTypes) {
                    key.append("|");
                    key.append(argType.getCanonicalName());
                    flag.append(argType.getCanonicalName());
                    flag.append(", ");
                }
                flag.delete(flag.length() - 2, flag.length());
                flag.append(")");
            }
            methodMap.put(key.toString(), method);
            methodFlagList.add(flag.toString());
            methodFlagList.sort(Collator.getInstance(Locale.ENGLISH));
        }
    }

    public static List<String> getMethodFlagList() {
        return methodFlagList;
    }

    private JedisPool jedisPool;
    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void init(String host, int port, String password, int db) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxWaitMillis(1000);
        config.setMaxIdle(30);
        config.setMinIdle(10);
        try {
            jedisPool = new JedisPool(config, host, port, 3000, password, db);
            connected = true;
            logger.info(String.format("Redis pool initialized. Host: %s, Port: %s.", host, port));
        } catch (Exception e) {
            logger.warn(String.format("Redis pool init failed, message: %s", e.getMessage()), e);
            connected = false;
        }
    }

    public void destroy() {
        if (jedisPool != null) {
            jedisPool.close();
            jedisPool.destroy();
        }
    }

    private static void releaseJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public boolean acquireLock(String lock, long expired, int tryTime) {
        long beginTime = System.currentTimeMillis();
        boolean acquired = acquireLock(lock, expired);
        while (!acquired) {
            acquired = acquireLock(lock, expired);
            if ((System.currentTimeMillis() - beginTime) >= tryTime) {
                return acquired;
            }
        }
        return true;
    }

    public boolean acquireLock(String lock, long expired) {
        Jedis jedis = null;
        boolean success = false;
        try {
            jedis = jedisPool.getResource();
            long value = System.currentTimeMillis() + expired + 1;
            long acquired = jedis.setnx(lock, String.valueOf(value));
            if (acquired == 1) {
                success = true;
            } else {
                String oldValue = jedis.get(lock);
                if (oldValue != null && Long.valueOf(oldValue) < System.currentTimeMillis()) {
                    String getValue = jedis.getSet(lock, String.valueOf(value));
                    if (getValue.equals(oldValue)) {
                        success = true;
                    }
                }
            }
        } catch (Exception e) {
            logger.warn(String.format("Exception happened while acquiring lock, message: %s", e.getMessage()), e);
        } finally {
            releaseJedis(jedis);
        }
        return success;
    }

    public void releaseLock(String lock) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            long current = System.currentTimeMillis();
            if (jedis.get(lock) != null && current < Long.valueOf(jedis.get(lock))) {
                jedis.del(lock);
            }
        } catch (Exception e) {
            logger.warn(String.format("Exception happened while releasing lock, message: %s", e.getMessage()), e);
        } finally {
            releaseJedis(jedis);
        }
    }


    public Object operate(String methodName, String[] argTypes, Object[] args) {
        if (methodName == null || methodName.isEmpty()) {
            return null;
        }
        Jedis jedis = null;
        try {
            StringBuilder key = new StringBuilder();
            key.append(methodName);
            if (argTypes != null && argTypes.length > 0) {
                for (String argType : argTypes) {
                    key.append("|");
                    key.append(argType);
                }
            }
            Method method = methodMap.get(key.toString());
            if (method == null) {
                return String.format("No func found with name: %s", methodName);
            }
            jedis = jedisPool.getResource();
            return method.invoke(jedis, args);
        } catch (Exception e) {
            logger.warn(String.format("Exception happened while operating jedis, message: %s", e.getMessage()), e);
            return e.getMessage();
        } finally {
            releaseJedis(jedis);
        }
    }
}


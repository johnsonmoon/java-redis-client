package com.github.johnsonmoon.java.redis.client.entity;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Create by xuyh at 2021/1/8 15:59.
 */
public class OperationParam {
    @NotNull
    private String methodName;
    private String[] argTypes;
    private Object[] args;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(String[] argTypes) {
        this.argTypes = argTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "JedisOperationParam{" +
                "methodName='" + methodName + '\'' +
                ", argTypes=" + Arrays.toString(argTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}

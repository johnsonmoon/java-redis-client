package com.github.johnsonmoon.java.redis.client.controller;

import com.github.johnsonmoon.java.redis.client.entity.OperationParam;
import com.github.johnsonmoon.java.redis.client.service.RedisService;
import com.github.johnsonmoon.java.redis.client.util.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * Create by xuyh at 2020/5/28 17:29.
 */
@Api("Redis Func")
@RestController
@RequestMapping("/redis")
public class ClientController {
    @Autowired
    private RedisService redisService;

    @ApiOperation("List functions")
    @GetMapping("/funcs")
    public Map<String, Object> funcList() {
        return MapUtils.builder().put("funcs", redisService.supportedFuncs()).build();
    }

    @ApiOperation("Operate with functions.")
    @PostMapping("/operate")
    public Map<String, Object> operate(@RequestBody OperationParam param) {
        return MapUtils.builder()
                .put("param", param.toString())
                .put("result", redisService.operate(param)).build();
    }
}

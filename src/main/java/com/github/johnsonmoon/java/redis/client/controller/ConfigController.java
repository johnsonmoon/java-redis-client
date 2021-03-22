package com.github.johnsonmoon.java.redis.client.controller;

import com.github.johnsonmoon.java.redis.client.common.ConfigurationModifiedEventSource;
import com.github.johnsonmoon.java.redis.client.common.Configurations;
import com.github.johnsonmoon.java.redis.client.util.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Create by xuyh at 2021/3/22 10:28.
 */
@Api("Configuration Management")
@RestController
@RequestMapping("/configs")
public class ConfigController {
    @ApiOperation("Get configurations.")
    @GetMapping
    public Map<String, Object> getConfigList() {
        Map<String, Object> map = MapUtils.builder().build();
        Configurations.getInstance().forEach(map::put);
        return map;
    }

    @ApiOperation("Modify configurations.")
    @PostMapping
    public Map<String, Object> modifyConfigs(@RequestBody Map<String, Object> params) {
        boolean modified = false;
        if (!params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    if (!Configurations.getInstance().getConf(key).equals(value)) {
                        modified = true;
                        Configurations.getInstance().putConf(key, value);
                    }
                }
            }
        }
        boolean notified = false;
        if (modified) {
            notified = true;
            ConfigurationModifiedEventSource.doOnModified();
        }
        return MapUtils.builder().put("modified", modified).put("notified", notified).build();
    }
}

package com.example.deviceservice.controller;

import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.deviceservice.config.DeviceRabbitMQConfig;
import com.example.model.common.R;
import com.example.model.devices.Devices;
import com.example.deviceservice.service.DeviceService;
import com.example.service.util.CacheRefreshService;
import com.example.service.util.RabbitMQUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/device")
@RestController
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RabbitMQUtils rabbitMQUtils;
    @Autowired
    private DeviceRabbitMQConfig deviceRabbitMQConfig;
    @Autowired
    private CacheRefreshService cacheRefreshService;

    /**
     * 获取设备列表
     * @return
     */
    @GetMapping("/list")
    public R list() {
        String key = "hot:device:list";
        List<Devices> d = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Devices>>() {},
                () -> deviceService.list(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME, DeviceRabbitMQConfig.ROUTING_KEY, cacheKey));
        return R.success(d);
    }

    /**
     * 两周使用总情况
     * @return
     */
    @GetMapping("/twoweek")
    public R twoweek() {
        String key = "hot:device:twoweek";
        Integer[] integers = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<Integer[]>() {
                },
                () -> deviceService.twoweek(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME, DeviceRabbitMQConfig.ROUTING_KEY, cacheKey));
        return R.success(integers);
    }
    /**
     * 获取在线设备数量
     * @return
     */
    @GetMapping("/getonline")
    public R getonline() {
        String key = "hot:device:getonline";
        Integer i = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<Integer>() {
                },
                () -> deviceService.countOnlineDevices(),
                60, 10,
                cacheKey -> rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME, DeviceRabbitMQConfig.ROUTING_KEY, cacheKey)
        );
        return R.success(i);
    }
    /**
     * 获取设备状态
     * @return
     */
    @GetMapping("/getstatus")
    public R getstatus() {
        String key = "hot:device:getstatus";
        List<Map<String, Object>> maps = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Map<String, Object>>>() {
                },
                () -> deviceService.countStatusDevices(),
                60, 10,
                cacheKey -> rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME, DeviceRabbitMQConfig.ROUTING_KEY, cacheKey));
        return R.success(maps);
    }

    /**
     * 周内开启设备占比
     * @return
     */
    @GetMapping("/weekuserate")
    public R weekuserate() {
        String key = "hot:device:weekuserate";
        List<Map<String, Object>> maps = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Map<String, Object>>>() {
                },
                () -> deviceService.weekOnline(),
                60, 10,
                cacheKey -> rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME, DeviceRabbitMQConfig.ROUTING_KEY, cacheKey));
        return R.success( maps);
    }
    /**
     * 查询设备
     * @return
     */
    @GetMapping("/getlist")
    public R getlist(@RequestParam(required = false) String name, @RequestParam(required = false) String status) {
        String key = "hot:device:getlist";
        rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME, DeviceRabbitMQConfig.ROUTING_KEY, key);
        return R.success();
    }
} 
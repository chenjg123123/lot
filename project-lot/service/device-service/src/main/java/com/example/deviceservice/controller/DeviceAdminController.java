package com.example.deviceservice.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.example.deviceservice.config.DeviceRabbitMQConfig;
import com.example.model.common.AsyncMessage;
import com.example.model.common.R;
import com.example.model.devices.Devices;
import com.example.deviceservice.service.DeviceService;
import com.example.service.util.CacheRefreshService;
import com.example.service.util.RabbitMQUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/device/admin")
@RestController
public class DeviceAdminController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RabbitMQUtils rabbitMQUtils;
    @Autowired
    private DeviceRabbitMQConfig deviceRabbitMQConfig;
    @Autowired
    private CacheRefreshService cacheRefreshService;

    @PostMapping("/add")
    public R add(@RequestBody AsyncMessage<Map<String, Object>> message) {
        String key = "operation:device:add";
        message.setType(key);
        rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME,
                deviceRabbitMQConfig.ROUTING_KEY_SELECT,
                message);
        return R.success();
    }
    @PostMapping("/delete")
    public R delete(@RequestBody AsyncMessage<Map<String, Object>> message) {
        String key = "operation:device:delete";
        message.setType(key);
        rabbitMQUtils.sendObject(deviceRabbitMQConfig.EXCHANGE_NAME,
                deviceRabbitMQConfig.ROUTING_KEY_SELECT,
                message);
        return R.success();
    }
}
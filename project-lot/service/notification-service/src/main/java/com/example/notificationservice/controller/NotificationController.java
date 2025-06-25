package com.example.notificationservice.controller;

import com.alibaba.fastjson2.TypeReference;
import com.example.model.Notification.Notifications;
import com.example.model.common.R;
import com.example.notificationservice.config.NotificationRabbitMQConfig;
import com.example.notificationservice.service.NotificationService;
import com.example.service.util.CacheRefreshService;
import com.example.service.util.RabbitMQUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/notification")
@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRabbitMQConfig notificationRabbitMQConfig;
    @Autowired
    private RabbitMQUtils rabbitMQUtils;
    @Autowired
    private CacheRefreshService cacheRefreshService;
    /**
     * 获取全部警告
     * @return
     */
    @GetMapping("/list")
    public R list() {
        String key = "hot:notification:list";
        List<Notifications> notifications = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Notifications>>() {
                },
                () -> notificationService.list(),
                60, 10,
                cacheKey -> rabbitMQUtils.sendObject(notificationRabbitMQConfig.EXCHANGE_NAME, NotificationRabbitMQConfig.ROUTING_KEY, cacheKey));
        return R.success(notifications);
    }
    /**
     * 两周每周总提醒数量
     * @return
     */
    @GetMapping("/twoweek")
    public R twoweek() {
        String key = "hot:notification:twoweek";
        Integer[] i = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<Integer[]>() {},
                ()-> notificationService.twoweek(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(notificationRabbitMQConfig.EXCHANGE_NAME, NotificationRabbitMQConfig.ROUTING_KEY, cacheKey)
                );
        return R.success(i);
    }

    /**
     * 每周警告使用情况
     * @return
     */
    @GetMapping("/weekuse")
    public R weekUse() {
        String key = "hot:notification:weekuse";
        List<Map<String, Object>> maps = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Map<String, Object>>>() {},
                ()-> notificationService.weekuse(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(notificationRabbitMQConfig.EXCHANGE_NAME, NotificationRabbitMQConfig.ROUTING_KEY, cacheKey)
                );
        return R.success(maps);
    }

} 
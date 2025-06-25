package com.example.notificationservice.mq;

import com.example.model.Notification.Notifications;
import com.example.notificationservice.service.NotificationService;
import com.example.service.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NotificationCacheConsumer {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 监听 device 服务专属队列 hot.device.queue
     */@RabbitListener(queues = "hot.notification.queue")
    public void handleMessage(String cacheKey) {
        log.info("收到缓存刷新请求: " + cacheKey);
        cacheKey = cacheKey.replace("\"", "");
        switch (cacheKey) {
            case "hot:notification:list":
                List<Notifications> list = notificationService.list();
                redisUtils.setObject(cacheKey, list, 5, TimeUnit.MINUTES);
                break;
            case "hot:notification:twoweek":
                Integer[] count = notificationService.twoweek();
                redisUtils.setObject(cacheKey, count, 5, TimeUnit.MINUTES);
                break;
            case "hot:notification:weekuse":
                List<Map<String, Object>> status = notificationService.weekuse();
                redisUtils.setObject(cacheKey, status, 5, TimeUnit.MINUTES);
                break;
            default:
                log.warn("未知的缓存刷新请求: " + cacheKey);
                break;
        }
    }

}

package com.example.deviceservice.mq;

import com.example.deviceservice.service.DeviceService;
import com.example.model.devices.Devices;
import com.example.service.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DeviceCacheConsumer {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 监听 device 服务专属队列 hot.device.queue
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "hot.device.queue", durable = "true"),
            exchange = @Exchange(value = "hot.exchange", type ="direct"),
            key = {"hot.device.refresh"}
    ))
    public void handleMessage(String cacheKey) {
        log.info("收到缓存刷新请求: " + cacheKey);
        cacheKey = cacheKey.replace("\"", "");
        switch (cacheKey) {
            case "hot:device:list":
                List<Devices> list = deviceService.list();
                redisUtils.setObject(cacheKey, list, 5, TimeUnit.MINUTES);
                break;
            case "hot:device:getonline":
                Integer count = deviceService.countOnlineDevices();
                redisUtils.setObject(cacheKey, count, 5, TimeUnit.MINUTES);
                break;
            case "hot:device:getstatus":
                List<Map<String, Object>> status = deviceService.countStatusDevices();
                redisUtils.setObject(cacheKey, status, 5, TimeUnit.MINUTES);
                break;
            case "hot:device:week":
                Integer[] week = deviceService.twoweek();
                redisUtils.setObject(cacheKey, week, 5, TimeUnit.MINUTES);
                break;
            case "hot:device:weekuserate":
                List<Map<String, Object>> weekuserate = deviceService.weekOnline();
                redisUtils.setObject(cacheKey, weekuserate, 5, TimeUnit.MINUTES);
                break;

            case "hot:device:twoweek":
                Integer[] twoweek = deviceService.twoweek();
                redisUtils.setObject(cacheKey, twoweek, 5, TimeUnit.MINUTES);
                break;
            default:
                log.warn("未知的缓存刷新请求: " + cacheKey);
                break;
        }
    }

}



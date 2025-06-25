package com.example.ticketservice.mq;

import com.example.model.ticket.Tickets;
import com.example.service.util.RedisUtils;
import com.example.ticketservice.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TicketCacheConsumer {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 监听 device 服务专属队列 hot.device.queue
     */@RabbitListener(queues = "hot.ticket.queue")
    public void handleMessage(String cacheKey) {
        log.info("收到缓存刷新请求: " + cacheKey);
        cacheKey = cacheKey.replace("\"", "");
        switch (cacheKey) {
            case "hot:ticket:list":
                List<Tickets> list = ticketService.list();
                redisUtils.setObject(cacheKey, list, 5, TimeUnit.MINUTES);
                break;
            case "hot:ticket:twoweek":
                Integer[] count = ticketService.twoweek();
                redisUtils.setObject(cacheKey, count, 5, TimeUnit.MINUTES);
                break;
            case "hot:ticket:weekuse":
                List<Map<String, Object>> status = ticketService.weekUse();
                redisUtils.setObject(cacheKey, status, 5, TimeUnit.MINUTES);
                break;
            case "hot:ticket:weekstatus":
                List<Map<String, Object>> weekStatus = ticketService.weekStatus();
                redisUtils.setObject(cacheKey, weekStatus, 5, TimeUnit.MINUTES);
                break;
            case "hot:ticket:weekticketeffiency":
                List<Map<String, Object>> weekTicketEffiency = ticketService.weekTicketEffiency();
                redisUtils.setObject(cacheKey, weekTicketEffiency, 5, TimeUnit.MINUTES);
                break;
            default:
                log.warn("未知的缓存刷新请求: " + cacheKey);
                break;
        }
    }

}

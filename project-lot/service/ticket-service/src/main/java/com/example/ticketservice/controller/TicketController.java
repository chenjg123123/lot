package com.example.ticketservice.controller;

import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.model.common.R;
import com.example.model.ticket.Tickets;
import com.example.service.util.CacheRefreshService;
import com.example.service.util.RabbitMQUtils;
import com.example.service.util.RedisUtils;
import com.example.ticketservice.config.TicketRabbitMQConfig;
import com.example.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/ticket")
@RestController
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private RabbitMQUtils rabbitMQUtils;
    @Autowired
    private TicketRabbitMQConfig ticketRabbitMQConfig;
    @Autowired
    private CacheRefreshService cacheRefreshService;


    /**
     * 获取全部工单
     * @return
     */
    @GetMapping("/list")
    public R list() {
        String key = "hot:ticket:list";
        List< Tickets> tickets = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List< Tickets>>() {
                },
                ()-> ticketService.list(),
                60,10,
                cacheKey -> rabbitMQUtils.sendObject(ticketRabbitMQConfig.EXCHANGE_NAME, ticketRabbitMQConfig.ROUTING_KEY, cacheKey));
        return R.success(tickets);
    }

    /**
     * 获取两周每周工单数量
     * @return
     */
    @GetMapping("/twoweek")
    public R twoweek() {
        String key = "hot:ticket:twoweek";
        Integer[] integers = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<Integer[]>() {},
                () -> ticketService.twoweek(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(ticketRabbitMQConfig.EXCHANGE_NAME, ticketRabbitMQConfig.ROUTING_KEY, cacheKey)
        );
        return R.success(integers);
    }

    /**
     * 获取一周内使用情况
     * @return
     */
    @GetMapping("/weekuse")
    public R weekUse() {
        String key = "hot:ticket:weekuse";
        List<Map<String, Object>> maps = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Map<String, Object>>>() {
                },
                () -> ticketService.weekUse(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(ticketRabbitMQConfig.EXCHANGE_NAME, ticketRabbitMQConfig.ROUTING_KEY, cacheKey)
        );
        return R.success(maps);
    }

    /**
     * 获取周一内工单状态
     * @return
     */
    @GetMapping("/weekstatus")
    public R weekStatus() {
        String key = "hot:ticket:weekstatus";
        List<Map<String, Object>> maps = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Map<String, Object>>>() {
                },
                () -> ticketService.weekStatus(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(ticketRabbitMQConfig.EXCHANGE_NAME, ticketRabbitMQConfig.ROUTING_KEY, cacheKey)
                );
        return R.success(maps);
    }
    /**
     * 获取一周内工单效率
     * @return
     */
    @GetMapping("/weekticketeffiency")
    public R weekTicketEffiency() {
        String key = "hot:ticket:weekticketeffiency";
        List<Map<String, Object>> maps = cacheRefreshService.cacheWithAsyncRefresh(key,
                new TypeReference<List<Map<String, Object>>>() {
                },
                () -> ticketService.weekTicketEffiency(),
                60,
                10,
                cacheKey -> rabbitMQUtils.sendObject(ticketRabbitMQConfig.EXCHANGE_NAME, ticketRabbitMQConfig.ROUTING_KEY, cacheKey)
        );
        return R.success(maps);
    }

} 